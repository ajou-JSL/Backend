package study.moum.community.likes.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import study.moum.auth.domain.entity.MemberEntity;
import study.moum.auth.domain.repository.MemberRepository;
import study.moum.community.article.domain.ArticleCategories;
import study.moum.community.article.domain.ArticleDetailsEntity;
import study.moum.community.article.domain.ArticleEntity;
import study.moum.community.article.domain.ArticleRepository;
import study.moum.community.article.dto.ArticleDto;
import study.moum.community.likes.domain.LikesEntity;
import study.moum.community.likes.domain.LikesRepository;
import study.moum.community.likes.dto.LikesDto;
import study.moum.global.error.ErrorCode;
import study.moum.global.error.exception.CustomException;
import study.moum.global.error.exception.NoAuthorityException;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static reactor.core.publisher.Mono.when;

public class LikesServiceTest {

    @InjectMocks
    private LikesService likesService;

    @Mock
    private LikesRepository likesRepository;

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private MemberRepository memberRepository;

    private MemberEntity member;
    private ArticleEntity article;
    private LikesEntity likes;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Mock Member
        member = MemberEntity.builder()
                .id(3)
                .username("testuser")
                .build();

        // Mock Article
        article = ArticleEntity.builder()
                .id(6)
                .likesCount(0)
                .build();

        // Mock LikesEntity
        likes = LikesEntity.builder()
                .id(1)
                .article(article)
                .member(member)
                .build();
    }

    @Test
    @DisplayName("좋아요 등록 성공")
    void create_likes_success() {
        // given
        String memberName = member.getUsername();
        int articleId = article.getId();

        // repository 기능 mocking
        given(memberRepository.findByUsername(memberName)).willReturn(member);
        given(articleRepository.findById(articleId)).willReturn(Optional.of(article));
        given(likesRepository.save(any(LikesEntity.class))).willReturn(likes);

        // when
        LikesDto.Response response = likesService.createLikes(memberName, articleId);

        // then
        verify(likesRepository).save(any(LikesEntity.class));
        verify(articleRepository).save(article);
        assertEquals(3, response.getMemberId());
        assertEquals(6, response.getArticleId());
    }


    @Test
    @DisplayName("좋아요 등록 실패 - 없는 게시글")
    void create_likes_fail_no_article() {
        // given
        String memberName = member.getUsername();
        int articleId = 999; // 존재하지 않는 게시글 ID

        // Mocking the behavior of repositories
        given(memberRepository.findByUsername(memberName)).willReturn(member);
        given(articleRepository.findById(articleId)).willReturn(Optional.empty()); // 게시글이 없음

        // then
        assertThrows(CustomException.class, () -> {
            likesService.createLikes(memberName, articleId); // 존재하지 않는 게시글에 대해 삭제 시도
        });
        assertEquals("게시글을 찾을 수 없습니다.",ErrorCode.ARTICLE_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("좋아요 삭제 성공")
    void delete_likes_success(){
        // given
        LikesEntity likesEntity = LikesEntity.builder()
                .id(1)
                .article(article)
                .member(member)
                .build();

        // Mocking the behavior of repositories
        given(likesRepository.findById(1)).willReturn(Optional.of(likesEntity)); // 다른 사용자의 좋아요를 찾음
        given(articleRepository.findById(article.getId())).willReturn(Optional.of(article)); // 게시글 존재

        when(likesService.checkAuth(member.getUsername(),article.getAuthor().getUsername())).thenReturn(true);

        LikesDto.Response response = likesService.deleteLikes(member.getUsername(), article.getId());

        // then
        verify(likesRepository).deleteById(1);
        assertNull(response);

    }

    @Test
    @DisplayName("좋아요 삭제 실패 - 없는 게시글")
    void delete_likes_fail_no_article(){
        // given
        String memberName = member.getUsername(); // 좋아요 삭제를 시도하는 사용자
        int articleId = 999; // 존재하지 않는 게시글 ID

        // Mocking the behavior of repositories
        given(likesRepository.findById(1)).willReturn(Optional.of(likes)); // 좋아요는 존재함
        given(articleRepository.findById(articleId)).willReturn(Optional.empty()); // 게시글이 없음

        // when & then
        assertThrows(CustomException.class, () -> {
            likesService.deleteLikes(memberName, articleId); // 존재하지 않는 게시글에 대해 삭제 시도
        });

        // likesRepository.deleteById(1)가 호출되지 않았는지 확인
        verify(likesRepository, never()).deleteById(1);

    }

    @Test
    @DisplayName("좋아요 삭제 실패 - 다른 사용자의 좋아요.권한 없음")
    void delete_likes_fail_different_user() {
        // given
        String memberName = member.getUsername(); // 삭제를 시도하는 사용자
        String anotherName = "another";
        int articleId = article.getId();

        // Mock LikesEntity for another user
        LikesEntity anotherUserLikes = LikesEntity.builder()
                .id(1)
                .article(article)
                .member(member) // 다른 사용자의 좋아요
                .build();

        // Mocking the behavior of repositories
        when(likesService.checkAuth(memberName,anotherName)).thenReturn(false);
        // given(likesRepository.findById(1)).willReturn(Optional.of(anotherUserLikes)); // 다른 사용자의 좋아요를 찾음
        given(articleRepository.findById(articleId)).willReturn(Optional.of(article)); // 게시글 존재

        // when & then
        assertThrows(NoAuthorityException.class, () -> {
            likesService.deleteLikes(memberName, articleId); // 자신의 좋아요가 아님
        });

        // likesRepository.deleteById(1)가 호출되지 않았는지 확인
        verify(likesRepository, never()).deleteById(1);
    }
}
