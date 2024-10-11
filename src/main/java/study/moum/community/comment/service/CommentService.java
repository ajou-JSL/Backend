package study.moum.community.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.moum.auth.domain.entity.MemberEntity;
import study.moum.auth.domain.repository.MemberRepository;
import study.moum.community.article.domain.ArticleDetailsEntity;
import study.moum.community.article.domain.ArticleDetailsRepository;
import study.moum.community.article.domain.ArticleEntity;
import study.moum.community.article.domain.ArticleRepository;
import study.moum.community.comment.domain.CommentEntity;
import study.moum.community.comment.domain.CommentRepository;
import study.moum.community.comment.dto.CommentDto;
import study.moum.global.error.ErrorCode;
import study.moum.global.error.exception.CustomException;
import study.moum.global.error.exception.NeedLoginException;
import study.moum.global.error.exception.NoAuthorityException;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final ArticleDetailsRepository articleDetailsRepository;
    private final MemberRepository memberRepository;

    public CommentDto.Response createComment(CommentDto.Request commentRequestDto, String username, int articleId){

        MemberEntity author = findUser(username);
        ArticleEntity article = getArticle(articleId);
        ArticleDetailsEntity articleDetails = getArticleDetails(articleId);

        checkAuthor(username, article.getAuthor().getUsername());

        CommentDto.Request commentRequest = CommentDto.Request.builder()
                .articleDetails(articleDetails)
                .author(author)
                .content(commentRequestDto.getContent())
                .build();


        // 댓글dto -> entity 변환 후 댓글 저장
        CommentEntity newComment = commentRequest.toEntity();
        commentRepository.save(newComment);

        // 게시글에 조회수 +1 하고 저장
        article.commentsCountUp();
        articleRepository.save(article);

        // 게시글_상세 테이블에 댓글 추가됐으니 게시글_상세 저장
        articleDetailsRepository.save(articleDetails);

        return new CommentDto.Response(newComment);

    }

    private MemberEntity findUser(String username){
        MemberEntity author = memberRepository.findByUsername(username);
        if(author == null){
            throw new NeedLoginException();
        }
        return author;
    }

    private ArticleDetailsEntity getArticleDetails(int articleDetailsId) {
        return articleDetailsRepository.findById(articleDetailsId)
                .orElseThrow(() -> new CustomException(ErrorCode.ARTICLE_NOT_FOUND));
    }

    private ArticleEntity getArticle(int articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new CustomException(ErrorCode.ARTICLE_NOT_FOUND));
    }

    private void checkAuthor(String memberName, String articleAuthor) {
        if (!memberName.equals(articleAuthor)) {
            throw new NoAuthorityException();
        }
    }
}
