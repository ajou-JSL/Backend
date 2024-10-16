package study.moum.community.likes.service;

import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;
import study.moum.auth.domain.entity.MemberEntity;
import study.moum.auth.domain.repository.MemberRepository;
import study.moum.community.article.domain.ArticleEntity;
import study.moum.community.article.domain.ArticleRepository;
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
        MemberEntity member = findMember(memberName);

        LikesDto.Request likesRequest = LikesDto.Request.builder()
                .member(member)
                .article(article)
                .build();

        // 유저이름이 게시글작성자랑 똑같으면 에러
        if(memberName.equals(article.getAuthor().getUsername())){
            throw new CustomException(ErrorCode.CANNOT_CREATE_SELF_LIKES);
        }

        LikesEntity newLikes = likesRequest.toEntity();
        likesRepository.save(newLikes);

        // 좋아요 +1 후 저장
        article.updateLikesCount(1);
        articleRepository.save(article);

        return new LikesDto.Response(newLikes);
    }

    public LikesDto.Response deleteLikes(int likesId, String memberName) {

        // 찾기
        LikesEntity likesEntity = likesRepository.findById(likesId)
                        .orElseThrow(()->new CustomException(ErrorCode.LIKES_NOT_FOUND));
        ArticleEntity article = findArticle(likesEntity.getArticle().getId());

        // 유저이름이랑 좋아요누른사람이랑 같으면 삭제
        if(memberName.equals(likesEntity.getMember().getUsername())){
            likesRepository.deleteById(likesId);
            article.updateLikesCount(-1);
        }

        return new LikesDto.Response(likesEntity);
    }

    public ArticleEntity findArticle(int articleId){
        return articleRepository.findById(articleId)
                .orElseThrow(()-> new CustomException(ErrorCode.ARTICLE_NOT_FOUND));
    }

    public MemberEntity findMember(String memberName){

        if(memberRepository.findByUsername(memberName) == null){
            throw new CustomException(ErrorCode.MEMBER_NOT_EXIST);
        }
        return memberRepository.findByUsername(memberName);
    }

}
