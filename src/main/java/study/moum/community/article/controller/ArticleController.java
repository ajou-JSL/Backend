package study.moum.community.article.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.moum.community.article.dto.ArticleDto;
import study.moum.community.article.service.ArticleService;
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

//    @GetMapping("/community/article/{id}")
//    public ResponseEntity<ResultResponse> getArticleById(@PathVariable int id){
//        ResultResponse response = ResultResponse.of(ResponseCode.조회성공, articleDto);
//    }
}
