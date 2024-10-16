package study.moum.community.article.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import study.moum.auth.domain.CustomUserDetails;
import study.moum.community.article.domain.ArticleCategories;
import study.moum.community.article.dto.ArticleDetailsDto;
import study.moum.community.article.dto.ArticleDto;
import study.moum.community.article.service.ArticleService;
import study.moum.global.error.exception.NeedLoginException;
import study.moum.global.response.ResponseCode;
import study.moum.global.response.ResultResponse;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    /**
     * 게시글 목록 조회 API 엔드포인트.
     *
     * @return 게시글 목록을 포함한 응답 객체
     *
     * 모든 게시글 목록을 조회한 후, 성공 응답과 함께 반환
     */
    @GetMapping("/api/articles")
    public ResponseEntity<ResultResponse> getArticleList(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size) {
        List<ArticleDto.Response> articleList = articleService.getArticleList(page,size);

        ResultResponse response = ResultResponse.of(ResponseCode.ARTICLE_LIST_GET_SUCCESS, articleList);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }



    @GetMapping("/api/articles/{id}")
    public ResponseEntity<ResultResponse> getArticleById(@PathVariable int id){
        ArticleDetailsDto.Response  articleDetailsResponse = articleService.getArticleById(id);
        ResultResponse response = ResultResponse.of(ResponseCode.ARTICLE_GET_SUCCESS, articleDetailsResponse);

        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @PostMapping("/api/articles")
    public ResponseEntity<ResultResponse> postArticle(
            @Valid @RequestBody ArticleDto.Request articleRequestDto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails){

        if(customUserDetails == null){
            throw new NeedLoginException();
        }
        ArticleDto.Response articleResponse = articleService.postArticle(articleRequestDto, customUserDetails.getUsername());

        ResultResponse response = ResultResponse.of(ResponseCode.ARTICLE_POST_SUCCESS, articleResponse);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @PatchMapping("/api/articles/{id}")
    public ResponseEntity<ResultResponse> updateArticle(
            @PathVariable int id,
            @Valid @RequestBody ArticleDetailsDto.Request articleDetailsRequestDto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails){

        ArticleDetailsDto.Response articleResponse = articleService.updateArticleDetails(id, articleDetailsRequestDto, customUserDetails.getUsername());

        ResultResponse response = ResultResponse.of(ResponseCode.ARTICLE_UPDATE_SUCCESS, articleResponse);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<ResultResponse> deleteArticle(
            @PathVariable int id,
            @AuthenticationPrincipal CustomUserDetails customUserDetails){

        ArticleDto.Response articleResponse = articleService.deleteArticleDetails(id, customUserDetails.getUsername());

        ResultResponse response = ResultResponse.of(ResponseCode.ARTICLE_DELETE_SUCCESS, articleResponse);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }



    /**
     * 키워드를 사용하여 게시글을 검색하는 API.
     *
     * @param keyword 검색어
     * @return 검색된 게시글 리스트
     */
    @GetMapping("/api/articles/search")
    public ResponseEntity<ResultResponse> searchArticles(@RequestParam(required = false) String keyword,
                                                         @RequestParam(required = false) String category,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size
    ) {

        List<ArticleDto.Response> articleList = articleService.getArticleWithTitleSearch(keyword,category,page,size);

        ResultResponse response = ResultResponse.of(ResponseCode.ARTICLE_LIST_GET_SUCCESS, articleList);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    /**
     * 게시글 목록 카테고리 필터링 조회 API
     *
     * @param category 게시글 카테고리 (FREE_TALKING_BOARD 또는 RECRUIT_BOARD)
     * @return 게시글 리스트
     */
    @GetMapping("/api/articles/category")
    public ResponseEntity<ResultResponse> getArticles(
            @RequestParam(required = true) ArticleCategories category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        List<ArticleDto.Response> articleList = articleService.getArticlesByCategory(category,page,size);

        ResultResponse response = ResultResponse.of(ResponseCode.ARTICLE_LIST_GET_SUCCESS, articleList);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

}
