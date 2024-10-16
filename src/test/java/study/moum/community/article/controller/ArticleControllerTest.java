package study.moum.community.article.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import study.moum.auth.domain.CustomUserDetails;
import study.moum.auth.domain.entity.MemberEntity;
import study.moum.auth.domain.repository.MemberRepository;
import study.moum.community.article.domain.ArticleCategories;
import study.moum.community.article.dto.ArticleDetailsDto;
import study.moum.community.article.dto.ArticleDto;
import study.moum.community.article.service.ArticleService;
import study.moum.custom.WithMockCustomMember;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(ArticleController.class)  // 특정 컨트롤러만 로드
class ArticleControllerTest {

    @MockBean
    private ArticleService articleService;  // Service 의존성 Mock 설정

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUp() {
        // UTF-8 인코딩 필터 추가
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(new CharacterEncodingFilter("utf-8", true))
                .build();
    }
    @Test
    @DisplayName("게시글 목록 조회 테스트")
    void getArticleList() throws Exception {
        // given : 게시글 리스트 생성
        List<ArticleDto.Response> mockResponse = List.of(
                new ArticleDto.Response(1, "Title 1", ArticleCategories.FREE_TALKING_BOARD, 10, 5, 3, "author1"),
                new ArticleDto.Response(2, "Title 2", ArticleCategories.FREE_TALKING_BOARD, 15, 3, 4, "author2"),
                new ArticleDto.Response(3, "Title 3", ArticleCategories.FREE_TALKING_BOARD, 20, 2, 1, "author3")
        );

        // when
        when(articleService.getArticleList(0,10)).thenReturn(mockResponse);

        // then
        mockMvc.perform(get("/api/articles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].title").value("Title 1"))
                .andExpect(jsonPath("$.data[1].title").value("Title 2"))
                .andExpect(jsonPath("$.data[2].title").value("Title 3"));
    }

    @Test
    @DisplayName("카테고리별 게시글 목록 조회 테스트")
    void getArticlesByCategoryTest() throws Exception {
        ArticleCategories category = ArticleCategories.FREE_TALKING_BOARD;

        // given: 게시글 리스트 생성
        List<ArticleDto.Response> mockResponse = List.of(
                new ArticleDto.Response(1, "Title 1", ArticleCategories.FREE_TALKING_BOARD, 10, 5, 3, "author1"),
                new ArticleDto.Response(2, "Title 2", ArticleCategories.FREE_TALKING_BOARD, 15, 3, 4, "author2"),
                new ArticleDto.Response(3, "Title 3", ArticleCategories.RECRUIT_BOARD, 20, 2, 1, "author3")
        );

        // when: 서비스의 메서드 호출 시 mockResponse를 반환하도록 설정
        when(articleService.getArticlesByCategory(category,0,10)).thenReturn(mockResponse);

        // then: 카테고리별 게시글 목록 조회
        // mockMvc.perform(get("/api/articles/category")
        //                .param("category", category.name()))  // 카테고리 값을 파라미터로 추가
        mockMvc.perform(get("/api/articles/category?category=" + category))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].title").value("Title 1"))
                .andExpect(jsonPath("$.data[0].category").value("FREE_TALKING_BOARD"))
                .andExpect(jsonPath("$.data[1].title").value("Title 2"))
                .andExpect(jsonPath("$.data[1].category").value("FREE_TALKING_BOARD"));
    }


    @Test
    @DisplayName("게시글 상세 조회 테스트")
    void getArticleByIdTest() throws Exception {
        // given : Author 생성
        MemberEntity author = MemberEntity.builder()
                .id(1)
                .email("test@gmail.com")
                .username("testAuthor")
                .password("12345123")
                .role("ROLE_ADMIN")
                .build();

        // given : Article 생성
        ArticleDetailsDto.Response mockResponse = new ArticleDetailsDto.Response(
                1, "Title", "FREE_TALKING_BOARD", "Content", 10, 2, 3, author.getUsername(), List.of()
        );

        // when
        when(articleService.getArticleById(1)).thenReturn(mockResponse);

        // then
        mockMvc.perform(get("/api/articles/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("Title"))
                .andExpect(jsonPath("$.data.author").value("testAuthor"))
                .andExpect(jsonPath("$.data.category").value("FREE_TALKING_BOARD"))
                .andExpect(jsonPath("$.data.content").value("Content"));

    }

    @Test
    @DisplayName("게시글 검색 테스트")
    void searchArticlesTest() throws Exception {
        String keyword = "searchKeyword";
        String category = "FREE_TALKING_BOARD";
        List<ArticleDto.Response> mockResponse = List.of(new ArticleDto.Response(1, "Title", ArticleCategories.FREE_TALKING_BOARD, 10, 5, 3, "author"));

        when(articleService.getArticleWithTitleSearch(keyword, category,0,10)).thenReturn(mockResponse);

        mockMvc.perform(get("/api/articles/search?keyword=" + keyword + "&category=" + category))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].title").value("Title"));
    }


    @Test
    @DisplayName("게시글 생성 테스트")
    void postArticleTest() throws Exception {

         //given : Author 생성
        MemberEntity author = MemberEntity.builder()
                .id(1)
                .email("test@gmail.com")
                .username("testAuthor")
                .password("12345123")
                .role("ROLE_ADMIN")
                .build();

        CustomUserDetails customUserDetails = new CustomUserDetails(author);

        // given : Article 생성
        ArticleDto.Request request = ArticleDto.Request.builder()
                .id(1)
                .category(ArticleCategories.FREE_TALKING_BOARD)
                .title("test title")
                //.author(author)
                .build();

        ArticleDto.Response response = new ArticleDto.Response(1, "test title", ArticleCategories.FREE_TALKING_BOARD, 0, 0, 0, any());

        // when(articleService.postArticle(any(), Mockito.anyString())).thenReturn(response);
        when(articleService.postArticle(request, eq(customUserDetails.getUsername()))).thenReturn(response);

        // Then
        mockMvc.perform(post("/api/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(SecurityMockMvcRequestPostProcessors.user(customUserDetails.getUsername())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("test title"));

    }
}

