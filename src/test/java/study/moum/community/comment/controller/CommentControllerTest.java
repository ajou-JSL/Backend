//package study.moum.community.comment.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.stubbing.Answer;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import study.moum.auth.domain.CustomUserDetails;
//import study.moum.auth.domain.entity.MemberEntity;
//import study.moum.auth.domain.repository.MemberRepository;
//import study.moum.community.article.controller.ArticleController;
//import study.moum.community.article.domain.ArticleDetailsEntity;
//import study.moum.community.article.domain.ArticleEntity;
//import study.moum.community.article.service.ArticleService;
//import study.moum.community.comment.domain.CommentEntity;
//import study.moum.community.comment.dto.CommentDto;
//import study.moum.community.comment.service.CommentService;
//import study.moum.global.error.ErrorCode;
//import study.moum.global.error.exception.CustomException;
//import study.moum.global.error.exception.NoAuthorityException;
//import study.moum.global.response.ResponseCode;
//
//import java.util.ArrayList;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.*;
//
//import static org.mockito.Mockito.*;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//
//class CommentControllerTest {
//
//    @Mock
//    private CommentService commentService;
//
//    @InjectMocks
//    private CommentController commentController;
//
//    private MockMvc mockMvc;
//    private ObjectMapper objectMapper;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        mockMvc = MockMvcBuilders.standaloneSetup(commentController).build();
//        objectMapper = new ObjectMapper();
//    }
//
//    @Test
//    @DisplayName("댓글 생성 성공")
//    @WithMockUser(username = "testUser")
//    @Disabled("에러 수정 해야함")
//    void createComment_Success() throws Exception {
//
//
//        // 테스트용 사용자와 게시글_상세 생성
//        MemberEntity author = MemberEntity.builder()
//                .id(1)
//                .username("testuser")
//                .password("1234")
//                .email("test@user.com")
//                .build();
//
//        ArticleDetailsEntity articleDetails = ArticleDetailsEntity.builder()
//                .id(1)
//                .build();
//
//        // given
//        CommentDto.Request requestDto = CommentDto.Request.builder()
//                .content("Test comment content")
//                .build();
//
//        CommentDto.Response responseDto = new CommentDto.Response(
//                CommentEntity.builder()
//                        .id(1)
//                        .author(author)
//                        .articleDetails(articleDetails)
//                        .content("Test comment content")
//                        .build()
//        );
//
//         when(commentService.createComment(any(), eq("testuser"), eq(1)))
//                .thenReturn(responseDto);
//
//
//        // when & then
//        mockMvc.perform(post("/api/comments/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(requestDto))
//                        .with(csrf())) // CSRF 토큰 추가
//                        //.principal(() -> "testuser")) // AuthenticationPrincipal 대체
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.code").value(ResponseCode.COMMENT_CREATE_SUCCESS.getCode()))
//                .andExpect(jsonPath("$.data.commentId").value(1))
//                .andExpect(jsonPath("$.data.content").value("Test comment content"));
//
//    }
//
//
//    @Test
//    @DisplayName("댓글 생성 실패 - 없는 게시글")
//    @Disabled("에러 수정 해야함")
//    void createComment_Fail_NoArticle() throws Exception {
//        // given
//        CommentDto.Request requestDto = CommentDto.Request.builder()
//                .content("Test comment content")
//                .build();
//
//        when(commentService.createComment(any(), eq("testuser"), eq(1)))
//                .thenThrow(new CustomException(ErrorCode.ARTICLE_NOT_FOUND));
//
//        // when & then
//        mockMvc.perform(post("/api/comments/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(requestDto))
//                        .with(csrf())) // CSRF 토큰 추가
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.code").value(ErrorCode.ARTICLE_NOT_FOUND.getCode()));
//
//    }
//
//
//    @Test
//    @DisplayName("댓글 수정 성공")
//    @WithMockUser("testUser")
//    @Disabled("에러 수정 해야함")
//    void updateComment_Success() throws Exception {
//        // given
//        CommentDto.Request requestDto = CommentDto.Request.builder()
//                .content("Updated comment content")
//                .build();
//
//        CommentDto.Response responseDto = new CommentDto.Response(
//                CommentEntity.builder()
//                        .id(1)
//                        .author(author)
//                        .articleDetails(articleDetails)
//                        .content("Updated comment content")
//                        .build()
//        );
//
//        // when(memberRepository.findByUsername(any())).thenReturn(any());
//         when(commentService.updateComment(any(), eq("testuser"), eq(1)))
//                .thenReturn(responseDto);
//
//        // when & then
//        mockMvc.perform(patch("/api/comments/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(requestDto))
//                        .with(csrf()))
//                        //.principal(() -> "testuser"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(ResponseCode.COMMENT_UPDATE_SUCCESS.getCode()))
//                .andExpect(jsonPath("$.data.content").value("Updated comment content"));
//
//        verify(commentService, times(1))
//                .updateComment(any(), eq("testuser"), eq(1));
//    }
//
//
//    @Test
//    @DisplayName("댓글 수정 실패 - 권한 없음")
//    @Disabled("에러 수정 해야함")
//    void updateComment_Fail_NoAuthorization() throws Exception {
//        // given
//        CommentDto.Request requestDto = CommentDto.Request.builder()
//                .content("Updated comment content")
//                .build();
//
//        when(commentService.updateComment(any(), eq("testuser"), eq(1)))
//                .thenThrow(new NoAuthorityException());
//
//        // when & then
//        mockMvc.perform(patch("/api/comments/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(requestDto))
//                        .principal(() -> "testuser"))
//                .andExpect(status().isForbidden())
//                .andExpect(jsonPath("$.code").value(ErrorCode.NO_AUTHORITY.getCode()));
//
//        verify(commentService, times(1)).updateComment(any(), eq("testuser"), eq(1));
//    }
//
//
//    @Test
//    @DisplayName("댓글 수정 실패 - 없는 댓글에 수정 시도")
//    @Disabled("에러 수정 해야함")
//    void updateComment_Fail_NoComment() throws Exception {
//        // given
//        int nonExistentCommentId = 999;
//        CommentDto.Request updateRequest = CommentDto.Request.builder()
//                .content("수정된 댓글")
//                .build();
//
//        // mocking: 없는 댓글일 경우 예외 발생
//        when(commentService.updateComment(any(CommentDto.Request.class), anyString(), eq(nonExistentCommentId)))
//                .thenThrow(new CustomException(ErrorCode.COMMENT_NOT_FOUND));
//
//        // when & then
//        mockMvc.perform(patch("/api/comments/{id}", nonExistentCommentId)
//                        .contentType("application/json")
//                        .content(objectMapper.writeValueAsString(updateRequest))
//                        .with(user("testuser").password("password").roles("USER"))
//                )
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.code").value("C004"))
//                .andExpect(jsonPath("$.message").value("댓글을 찾을 수 없습니다."));
//    }
//
//
//    @Test
//    @DisplayName("댓글 삭제 성공")
//    @WithMockUser("testuser")
//    @Disabled("에러 수정 해야함")
//    void deleteComment_Success() throws Exception {
//        // given
//        MemberEntity author = MemberEntity.builder()
//                .id(1)
//                .email("test@user.com")
//                .username("testuser")
//                .password("1234")
//                .role("ROLE_ADMIN")
//                .build();
//
//        ArticleDetailsEntity articleDetails = ArticleDetailsEntity.builder()
//                .id(1)
//                .comments(new ArrayList<>())
//                .content("test content")
//                .articleId(1)
//                .build();
//
//        CommentEntity comment = CommentEntity.builder()
//                .id(1)
//                .articleDetails(articleDetails)
//                .author(author)
//                .content("test content")
//                .build();
//
//        CommentDto.Response responseDto = new CommentDto.Response(comment);
//
//        when(commentService.deleteComment(eq("testuser"), eq(1))).thenReturn(responseDto);
//
//        // when & then
//        mockMvc.perform(delete("/api/comments/1")
//                        .with(csrf()))  // CSRF protection
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(ResponseCode.COMMENT_DELETE_SUCCESS.getCode()))
//                .andExpect(jsonPath("$.data.commentId").value(1));
//    }
//
//
//
//    @Test
//    @DisplayName("댓글 삭제 실패 - 권한 없음")
//    @Disabled("에러 수정 해야함")
//    void deleteComment_Fail_NoAuthorization() throws Exception {
//        // given
//        int commentId = 1;
//
//        // mocking: 권한 없을 경우 예외 발생
//        doThrow(new NoAuthorityException())
//                .when(commentService).deleteComment(anyString(), eq(commentId));
//
//        // when & then
//        when(memberRepository.findByUsername(author.getUsername())).thenReturn(author);
//
//        mockMvc.perform(delete("/api/comments/{id}", commentId)
//                        .with(user("unauthorizedUser").password("password").roles("USER"))
//                )
//                .andExpect(status().isForbidden())
//                .andExpect(jsonPath("$.code").value("A003"))
//                .andExpect(jsonPath("$.message").value("권한이 없습니다."));
//    }
//
//    @Test
//    @DisplayName("댓글 삭제 실패 - 이미 삭제된 댓글")
//    @Disabled("에러 수정 해야함")
//    void deleteComment_Fail_AlreadyDeleted() throws Exception {
//        // given
//        int commentId = 1;
//
//        // mocking: 이미 삭제된 댓글일 경우 예외 발생
//        doThrow(new CustomException(ErrorCode.COMMENT_ALREADY_DELETED))
//                .when(commentService).deleteComment(anyString(), eq(commentId));
//
//        // when & then
//        mockMvc.perform(delete("/api/comments/{id}", commentId)
//                        .with(user("testuser").password("password").roles("USER"))
//                )
//                .andExpect(status().isGone())
//                .andExpect(jsonPath("$.code").value("C005"))
//                .andExpect(jsonPath("$.message").value("이미 삭제된 댓글입니다."));
//    }
//
//}