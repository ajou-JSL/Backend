package study.moum.community.article.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.moum.community.article.domain.ArticleRepository;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
}
