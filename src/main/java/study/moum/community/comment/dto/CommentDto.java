package study.moum.community.comment.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import study.moum.auth.domain.entity.MemberEntity;
import study.moum.community.article.domain.ArticleDetailsEntity;
import study.moum.community.article.domain.ArticleEntity;
import study.moum.community.comment.domain.CommentEntity;

public class CommentDto {

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Request{

        @NotEmpty @NotNull
        private String content;

        private MemberEntity author;
        private ArticleDetailsEntity articleDetails;

        public CommentEntity toEntity(){
            return CommentEntity.builder()
                    .content(content)
                    .articleDetails(articleDetails)
                    .author(author)
                    .build();
        }
    }

    @Getter
    public static class Response{

        private final int commentId;
        private final int articleDetailsId;
        private final String author;
        private final String content;

        public Response(CommentEntity comment){
            this.commentId = comment.getId();
            this.articleDetailsId = comment.getArticleDetails().getId();
            this.content = comment.getContent();
            this.author = comment.getAuthor().getUsername();
        }
    }
}
