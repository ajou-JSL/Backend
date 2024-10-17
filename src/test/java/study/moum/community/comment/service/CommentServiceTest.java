package study.moum.community.comment.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.test.context.support.WithMockUser;
import study.moum.auth.domain.entity.MemberEntity;
import study.moum.auth.domain.repository.MemberRepository;
import study.moum.community.article.domain.*;
import study.moum.community.comment.domain.CommentEntity;
import study.moum.community.comment.domain.CommentRepository;
import study.moum.community.comment.dto.CommentDto;
import study.moum.community.comment.service.CommentService;
import study.moum.global.error.ErrorCode;
import study.moum.global.error.exception.CustomException;
import study.moum.global.error.exception.NoAuthorityException;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommentServiceTest {

    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ArticleDetailsRepository articleDetailsRepository;

    @Mock
    private MemberRepository memberRepository;

    private MemberEntity author;
    private ArticleDetailsEntity articleDetails;
    private ArticleEntity article;
    private CommentEntity comment;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        // 테스트에 필요한 객체들 초기화
        author = MemberEntity.builder()
                .id(1)
                .email("test@user.com")
                .password("1234")
                .username("testuser")
                .role("ROLE_ADMIN")
                .build();

        article = ArticleEntity.builder()
                .id(1)
                .title("test title")
                .author(author)
                .category(ArticleCategories.RECRUIT_BOARD)
                .build();

        articleDetails = ArticleDetailsEntity.builder()
                .id(1)
                .comments(new ArrayList<>())
                .content("test content")
                .articleId(article.getId())
                .build();

        comment = CommentEntity.builder()
                .id(1)
                .articleDetails(articleDetails)
                .author(author)
                .content("test content")
                .build();
    }

    @Test
    @DisplayName("댓글 생성 성공")
    @WithMockUser(username = "testuser") // 로그인한 사용자 설정
    @Disabled("에러 수정해야함")
    void createComment_Success() {
        // Given
        CommentDto.Request requestDto = new CommentDto.Request("테스트 댓글", author, articleDetails);

        when(memberRepository.findByUsername("testuser")).thenReturn(author);
        when(articleDetailsRepository.findById(1)).thenReturn(Optional.of(articleDetails));
        when(commentRepository.save(any(CommentEntity.class))).thenReturn(comment);

        // When
        CommentDto.Response response = commentService.createComment(requestDto, "testuser", 1);

        // Then
        assertNotNull(response);
        assertEquals("테스트 댓글", response.getContent());
        verify(commentRepository, times(1)).save(any(CommentEntity.class));
    }

    @Test
    @DisplayName("댓글 수정 실패 - 없는 댓글")
    @WithMockUser(username = "testuser") // 로그인한 사용자 설정
    void updateComment_Fail_NoComment() {
        // Given
        CommentDto.Request requestDto = new CommentDto.Request("수정된 댓글 내용", author, articleDetails);

        when(commentRepository.findById(1)).thenReturn(Optional.empty());

        // When & Then
        CustomException exception = assertThrows(CustomException.class,
                () -> commentService.updateComment(requestDto, "testuser", 1));
        assertEquals(ErrorCode.COMMENT_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("댓글 삭제 실패 - 권한 없음")
    void deleteComment_Fail_NoAuthorization() {
        // Given
        when(memberRepository.findByUsername("testuser")).thenReturn(author);
        when(commentRepository.findById(1)).thenReturn(Optional.of(comment));

        // 다른 사용자가 작성한 댓글로 설정
        MemberEntity anotherUser = MemberEntity.builder()
                .id(1)
                .email("another@user.com")
                .password("1234")
                .username("anotherUser")
                .role("ROLE_ADMIN")
                .build();

        comment.setAuthor(anotherUser);

        // When & Then
        NoAuthorityException exception = assertThrows(NoAuthorityException.class,
                () -> commentService.deleteComment("testuser", 1));
        assertNotNull(exception);
    }

    @Test
    @DisplayName("댓글 삭제 실패 - 이미 삭제된 댓글")
    @WithMockUser(username = "testuser") // 로그인한 사용자 설정
    void deleteComment_Fail_AlreadyDeleted() {
        // Given
        when(commentRepository.findById(1)).thenReturn(Optional.empty());

        // When & Then
        CustomException exception = assertThrows(CustomException.class,
                () -> commentService.deleteComment("testuser", 1));
        assertEquals(ErrorCode.COMMENT_ALREADY_DELETED, exception.getErrorCode());
    }
}
