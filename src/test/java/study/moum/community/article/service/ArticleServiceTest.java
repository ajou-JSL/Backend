//package study.moum.community.article.service;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import study.moum.auth.domain.entity.MemberEntity;
//import study.moum.auth.domain.repository.MemberRepository;
//import study.moum.community.article.domain.*;
//import study.moum.community.article.dto.ArticleDetailsDto;
//import study.moum.community.article.dto.ArticleDto;
//import study.moum.global.error.exception.CustomException;
//import study.moum.global.error.exception.NeedLoginException;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//class ArticleServiceTest {
//
//    @InjectMocks
//    private ArticleService articleService;
//
//    @Mock
//    private ArticleRepository articleRepository;
//
//    @Mock
//    private ArticleDetailsRepository articleDetailsRepository;
//
//    @Mock
//    private MemberRepository memberRepository;
//
//    @Mock
//    private ArticleRepositoryCustom articleRepositoryCustom;
//
//    private MemberEntity author;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        author = new MemberEntity();
//        author.setUsername("testUser");
//    }
//
//    @Test
//    void postArticle_Success() {
//        // given
//        ArticleDto.Request articleRequestDto = ArticleDto.Request.builder()
//                .title("Test Title")
//                .category(ArticleCategories.FREE_TALKING)
//                .content("Test Content")
//                .build();
//
//        when(memberRepository.findByUsername("testUser")).thenReturn(author);
//        when(articleRepository.save(any(ArticleEntity.class))).thenReturn(new ArticleEntity());
//        when(articleDetailsRepository.save(any(ArticleDetailsEntity.class))).thenReturn(new ArticleDetailsEntity());
//
//        // when
//        ArticleDto.Response response = articleService.postArticle(articleRequestDto, "testUser");
//
//        // then
//        assertNotNull(response);
//        verify(articleRepository).save(any(ArticleEntity.class));
//        verify(articleDetailsRepository).save(any(ArticleDetailsEntity.class));
//    }
//
//    @Test
//    void postArticle_NeedLoginException() {
//        // given
//        ArticleDto.Request articleRequestDto = ArticleDto.Request.builder()
//                .title("Test Title")
//                .category(ArticleCategories.FREE_TALKING)
//                .content("Test Content")
//                .build();
//
//        // when
//        Exception exception = assertThrows(NeedLoginException.class, () -> {
//            articleService.postArticle(articleRequestDto, null);
//        });
//
//        // then
//        assertNotNull(exception);
//    }
//
//    @Test
//    void getArticleById_Success() {
//        // given
//        int articleId = 1;
//        ArticleDetailsEntity articleDetails = new ArticleDetailsEntity();
//        ArticleEntity article = new ArticleEntity();
//        article.setId(articleId);
//        article.setAuthor(author);
//        when(articleDetailsRepository.findById(articleId)).thenReturn(Optional.of(articleDetails));
//        when(articleRepository.findById(articleId)).thenReturn(Optional.of(article));
//
//        // when
//        ArticleDetailsDto.Response response = articleService.getArticleById(articleId);
//
//        // then
//        assertNotNull(response);
//        assertEquals(articleId, response.getId());
//        verify(articleDetailsRepository).findById(articleId);
//    }
//
//    @Test
//    void getArticleById_ArticleNotFound() {
//        // given
//        int articleId = 1;
//        when(articleDetailsRepository.findById(articleId)).thenReturn(Optional.empty());
//
//        // when
//        Exception exception = assertThrows(CustomException.class, () -> {
//            articleService.getArticleById(articleId);
//        });
//
//        // then
//        assertNotNull(exception);
//    }
//
//    @Test
//    void getArticleList_Success() {
//        // given
//        when(articleRepository.findAll()).thenReturn(List.of(new ArticleEntity()));
//
//        // when
//        List<ArticleDto.Response> articleList = articleService.getArticleList();
//
//        // then
//        assertNotNull(articleList);
//        assertFalse(articleList.isEmpty());
//    }
//
//    @Test
//    void updateArticleDetails_Success() {
//        // given
//        int articleId = 1;
//        ArticleDetailsDto.Request articleDetailsRequestDto = ArticleDetailsDto.Request.builder()
//                .title("Updated Title")
//                .content("Updated Content")
//                .category(ArticleCategories.FREE_TALKING)
//                .build();
//
//        ArticleDetailsEntity articleDetails = new ArticleDetailsEntity();
//        ArticleEntity article = new ArticleEntity();
//        article.setAuthor(author);
//        when(articleDetailsRepository.findById(articleId)).thenReturn(Optional.of(articleDetails));
//        when(articleRepository.findById(articleId)).thenReturn(Optional.of(article));
//
//        // when
//        ArticleDetailsDto.Response response = articleService.updateArticleDetails(articleId, articleDetailsRequestDto, "testUser");
//
//        // then
//        assertNotNull(response);
//        verify(articleDetailsRepository).save(articleDetails);
//    }
//
//    @Test
//    void updateArticleDetails_NoAuthority() {
//        // given
//        int articleId = 1;
//        ArticleDetailsDto.Request articleDetailsRequestDto = new ArticleDetailsDto.Request();
//        articleDetailsRequestDto.setTitle("Updated Title");
//
//        ArticleDetailsEntity articleDetails = new ArticleDetailsEntity();
//        ArticleEntity article = new ArticleEntity();
//        MemberEntity anotherAuthor = new MemberEntity();
//        anotherAuthor.setUsername("anotherUser");
//        article.setAuthor(anotherAuthor);
//        when(articleDetailsRepository.findById(articleId)).thenReturn(Optional.of(articleDetails));
//        when(articleRepository.findById(articleId)).thenReturn(Optional.of(article));
//
//        // when
//        Exception exception = assertThrows(CustomException.class, () -> {
//            articleService.updateArticleDetails(articleId, articleDetailsRequestDto, "testUser");
//        });
//
//        // then
//        assertNotNull(exception);
//    }
//
//    @Test
//    void deleteArticleDetails_Success() {
//        // given
//        int articleId = 1;
//        ArticleDetailsEntity articleDetails = new ArticleDetailsEntity();
//        ArticleEntity article = new ArticleEntity();
//        article.setAuthor(author);
//        when(articleDetailsRepository.findById(articleId)).thenReturn(Optional.of(articleDetails));
//        when(articleRepository.findById(articleId)).thenReturn(Optional.of(article));
//
//        // when
//        ArticleDto.Response response = articleService.deleteArticleDetails(articleId, "testUser");
//
//        // then
//        assertNotNull(response);
//        verify(articleDetailsRepository).deleteById(articleId);
//        verify(articleRepository).deleteById(articleId);
//    }
//
//    @Test
//    void deleteArticleDetails_NoAuthority() {
//        // given
//        int articleId = 1;
//        ArticleDetailsEntity articleDetails = new ArticleDetailsEntity();
//        ArticleEntity article = new ArticleEntity();
//        MemberEntity anotherAuthor = new MemberEntity();
//        anotherAuthor.setUsername("anotherUser");
//        article.setAuthor(anotherAuthor);
//        when(articleDetailsRepository.findById(articleId)).thenReturn(Optional.of(articleDetails));
//        when(articleRepository.findById(articleId)).thenReturn(Optional.of(article));
//
//        // when
//        Exception exception = assertThrows(CustomException.class, () -> {
//            articleService.deleteArticleDetails(articleId, "testUser");
//        });
//
//        // then
//        assertNotNull(exception);
//    }
//
//    @Test
//    void getFreeTalkingArticles_Success() {
//        // given
//        when(articleRepositoryCustom.findFreeTalkingArticles()).thenReturn(List.of(new ArticleEntity()));
//
//        // when
//        List<ArticleDto.Response> articles = articleService.getFreeTalkingArticles();
//
//        // then
//        assertNotNull(articles);
//        assertFalse(articles.isEmpty());
//    }
//
//    @Test
//    void getRecruitingArticles_Success() {
//        // given
//        when(articleRepositoryCustom.findRecruitingdArticles()).thenReturn(List.of(new ArticleEntity()));
//
//        // when
//        List<ArticleDto.Response> articles = articleService.getRecruitingArticles();
//
//        // then
//        assertNotNull(articles);
//        assertFalse(articles.isEmpty());
//    }
//
//    @Test
//    void getArticleWithTitleSearch_Success() {
//        // given
//        String keyword = "searchKeyword";
//        when(articleRepositoryCustom.searchArticlesByTitleKeyword(keyword, null)).thenReturn(List.of(new ArticleEntity()));
//
//        // when
//        List<ArticleDto.Response> articles = articleService.getArticleWithTitleSearch(keyword, null);
//
//        // then
//        assertNotNull(articles);
//        assertFalse(articles.isEmpty());
//    }
//}
