package study.moum.community.article.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import study.moum.auth.domain.entity.MemberEntity;
import study.moum.community.article.domain.ArticleCategories;
import study.moum.community.article.dto.ArticleDetailsDto;
import study.moum.community.article.dto.ArticleDto;
import study.moum.community.article.service.ArticleService;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

class ArticleControllerTest {

    @Mock
    private ArticleService articleService;

    @InjectMocks
    private ArticleController articleController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(articleController).build();
        objectMapper = new ObjectMapper();
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
        when(articleService.getArticleList()).thenReturn(mockResponse);

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
        when(articleService.getArticlesByCategory(category)).thenReturn(mockResponse);

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

        when(articleService.getArticleWithTitleSearch(keyword, category)).thenReturn(mockResponse);

        mockMvc.perform(get("/api/articles/search?keyword=" + keyword + "&category=" + category))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].title").value("Title"));
    }


    @Test
    @DisplayName("게시글 생성 테스트")
    @WithMockUser(username = "testUser")
    void postArticleTest() throws Exception {

        // given : Author 생성
        MemberEntity author = MemberEntity.builder()
                .id(1)
                .email("test@gmail.com")
                .username("testAuthor")
                .password("12345123")
                .role("ROLE_ADMIN")
                .build();

        // given : Article 생성
        ArticleDto.Request request = ArticleDto.Request.builder()
                .id(1)
                .category(ArticleCategories.FREE_TALKING_BOARD)
                .title("test title")
                .author(author)
                .build();

        ArticleDto.Response response = new ArticleDto.Response(1, "test title", ArticleCategories.FREE_TALKING_BOARD, 0, 0, 0, author.getUsername());

        // when(articleService.postArticle(any(), Mockito.anyString())).thenReturn(response);
        when(articleService.postArticle(request, author.getUsername())).thenReturn(response);

        // Then
        mockMvc.perform(post("/api/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf())) // CSRF 토큰 추가
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("test title"));
    }
}

