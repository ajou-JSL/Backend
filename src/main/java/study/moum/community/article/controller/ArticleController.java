package study.moum.community.article.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import study.moum.auth.domain.CustomUserDetails;
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

//    @GetMapping("/community/articles")
//    public ResponseEntity<ResultResponse> getAllArticles(){
//        List<ArticleDto.Response> articles = new ArrayList<>();
//        ResultResponse response = ResultResponse.of(ResponseCode.조회성공, articlesDto);
//    }

    @GetMapping("/community/article/{id}")
    public ResponseEntity<ResultResponse> getArticleById(@PathVariable int id){
        ArticleDetailsDto.Response  articleDetailsResponse = articleService.getArticleById(id);
        ResultResponse response = ResultResponse.of(ResponseCode.ARTICLE_ONE_GET_SUCCESS, articleDetailsResponse);

        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @PostMapping("/community/article")
    public ResponseEntity<ResultResponse> postArticle(
            @RequestBody ArticleDto.Request articleRequestDto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails){

        if(customUserDetails == null){
            throw new NeedLoginException();
        }
        ArticleDto.Response articleResponse = articleService.postArticle(articleRequestDto, customUserDetails.getUsername());

        ResultResponse response = ResultResponse.of(ResponseCode.ARTICLE_POST_SUCCESS, articleResponse);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @PatchMapping("/community/article/{id}")
    public ResponseEntity<ResultResponse> updateArticle(
            @PathVariable int id,
            @RequestBody ArticleDetailsDto.Request articleDetailsRequestDto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails){

        ArticleDetailsDto.Response articleResponse = articleService.updateArticleDetails(id, articleDetailsRequestDto, customUserDetails.getUsername());

        ResultResponse response = ResultResponse.of(ResponseCode.ARTICLE_UPDATE_SUCCESS, articleResponse);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @DeleteMapping("/community/article/{id}")
    public ResponseEntity<ResultResponse> deleteArticle(
            @PathVariable int id,
            @AuthenticationPrincipal CustomUserDetails customUserDetails){

        ArticleDto.Response articleResponse = articleService.deleteArticleDetails(id, customUserDetails.getUsername());

        ResultResponse response = ResultResponse.of(ResponseCode.ARTICLE_DELETE_SUCCESS, articleResponse);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

}
