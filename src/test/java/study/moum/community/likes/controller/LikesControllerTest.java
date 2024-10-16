package study.moum.community.likes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import study.moum.auth.domain.entity.MemberEntity;
import study.moum.community.article.domain.ArticleCategories;
import study.moum.community.article.domain.ArticleEntity;
import study.moum.community.likes.dto.LikesDto;
import study.moum.community.likes.service.LikesService;
import study.moum.email.controller.EmailController;
import study.moum.email.service.EmailService;
import study.moum.global.response.ResponseCode;
import study.moum.redis.util.RedisUtil;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LikesControllerTest {

    @InjectMocks
    private LikesController likesController;

    @Mock
    private LikesService likesService;

    private MemberEntity member;
    private ArticleEntity article;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(likesController)
                .alwaysDo(print())
                .build();
        objectMapper = new ObjectMapper();

        member = MemberEntity.builder()
                .id(1)
                .role("ROLE_USER")
                .username("testuser")
                .password("1234")
                .email("test@user.com")
                .build();

        article = ArticleEntity.builder()
                .id(1)
                .category(ArticleCategories.RECRUIT_BOARD)
                .title("test title")
                .author(member)
                .likesCount(0)
                .build();
    }

    @Test
    @DisplayName("게시글 좋아요 성공")
    @WithMockUser("testuser")
    void article_likes_success() throws Exception{
        // given
        LikesDto.Request likesRequestDto = LikesDto.Request.builder()
                .member(member)
                .article(article)
                .build();

        LikesDto.Response likesResponseDto = new LikesDto.Response(1,1,1);

        // when
        when(likesService.createLikes(member.getUsername(), article.getId())).thenReturn(likesResponseDto);

        // then
        mockMvc.perform(post("/api/likes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(likesRequestDto))
                        .with(csrf())) // CSRF 토큰 추가
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.message").value(ResponseCode.LIKES_CREATE_SUCCESS));

    }

    @Test
    @DisplayName("게시글 좋아요 실패 - 이미 좋아요 누름")
    void article_likes_fail_already_liked(){
        // given

        // when

        // then

    }

    @Test
    @DisplayName("게시글 좋아요 실패 - 삭제된 게시글")
    void article_likes_fail_deleted_article(){
        // given

        // when

        // then

    }

    @Test
    @DisplayName("게시글 좋아요 삭제 성공")
    void delete_likes_success(){
        // given

        // when

        // then

    }

    @Test
    @DisplayName("게시글 좋아요 삭제 실패 - 권한 없음")
    void delete_likes_fail_NoAuthority(){
        // given

        // when

        // then

    }

    @Test
    @DisplayName("게시글 좋아요 목록 조회")
    void get_likes_article_list(){
        // given

        // when

        // then

    }
}
