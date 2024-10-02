package study.moum.community.article.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import study.moum.community.article.service.ArticleService;

@RestController
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
}
