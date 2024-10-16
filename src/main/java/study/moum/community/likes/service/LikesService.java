package study.moum.community.likes.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.moum.auth.domain.entity.MemberEntity;
import study.moum.auth.domain.repository.MemberRepository;
import study.moum.community.article.domain.ArticleEntity;
import study.moum.community.article.domain.ArticleRepository;
import study.moum.community.comment.domain.CommentEntity;
import study.moum.community.comment.dto.CommentDto;
import study.moum.community.likes.domain.LikesEntity;
import study.moum.community.likes.domain.LikesRepository;
import study.moum.community.likes.dto.LikesDto;
import study.moum.global.error.ErrorCode;
import study.moum.global.error.exception.CustomException;

@Service
@RequiredArgsConstructor
public class LikesService {

    private final LikesRepository likesRepository;
    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;

    public LikesDto.Response createLikes(String memberName, int articleId) {

        ArticleEntity article = findArticle(articleId);
        MemberEntity member = findAMember(memberName);

        LikesDto.Request likesRequest = LikesDto.Request.builder()
                .member(member)
                .article(article)
                .build();

        LikesEntity newLikes = likesRequest.toEntity();
        likesRepository.save(newLikes);

        // 좋아요 +1 후 저장
        article.likesCountUp();
        articleRepository.save(article);

        return new LikesDto.Response(newLikes);
    }

    public ArticleEntity findArticle(int articleId){
        return articleRepository.findById(articleId)
                .orElseThrow(()-> new CustomException(ErrorCode.ARTICLE_NOT_FOUND));
    }

    public MemberEntity findAMember(String memberName){
        MemberEntity member = memberRepository.findByUsername(memberName);
        if(member == null){
            throw new CustomException(ErrorCode.MEMBER_NOT_EXIST);
        }
        return member;
    }



}
