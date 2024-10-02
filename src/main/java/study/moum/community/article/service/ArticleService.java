package study.moum.community.article.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.moum.auth.domain.entity.MemberEntity;
import study.moum.auth.domain.repository.MemberRepository;
import study.moum.community.article.domain.ArticleDetailsEntity;
import study.moum.community.article.domain.ArticleDetailsRepository;
import study.moum.community.article.domain.ArticleEntity;
import study.moum.community.article.domain.ArticleRepository;
import study.moum.community.article.dto.ArticleDetailsDto;
import study.moum.community.article.dto.ArticleDto;
import study.moum.global.error.ErrorCode;
import study.moum.global.error.exception.CustomException;
import study.moum.global.error.exception.MemberNotExistException;
import study.moum.global.error.exception.NeedLoginException;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleDetailsRepository articleDetailsRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ArticleDto.Response postArticle(ArticleDto.Request articleRequestDto, String memberName){
        MemberEntity author = memberRepository.findByUsername(memberName);
        if(author == null){
            throw new NeedLoginException();
        }

        ArticleDto.Request articleRequest = ArticleDto.Request.builder()
                .author(author)
                .title(articleRequestDto.getTitle())
                .build();

        ArticleEntity newArticle = articleRequest.toEntity();
        articleRepository.save(newArticle);


        ArticleDetailsDto.Request articleDetailsRequestDto = ArticleDetailsDto.Request.builder()
                .content(articleRequestDto.getContent())
                .build();

        ArticleDetailsEntity newArticleDetails = articleDetailsRequestDto.toEntity();
        articleDetailsRepository.save(newArticleDetails);

        return new ArticleDto.Response(newArticle);

    }

    @Transactional(readOnly = true)
    public ArticleDetailsDto.Response getArticleById(int articleDetailsId){
        ArticleDetailsEntity articleDetails = articleDetailsRepository.findById(articleDetailsId).orElseThrow(()->new CustomException(ErrorCode.ARTICLE_NOT_FOUND));
        ArticleEntity article = articleRepository.findById(articleDetailsId).orElseThrow(()->new CustomException(ErrorCode.ARTICLE_NOT_FOUND));

        article.viewCountUp();

        return new ArticleDetailsDto.Response(articleDetails, article);

    }
}
