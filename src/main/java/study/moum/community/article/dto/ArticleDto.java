package study.moum.community.article.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import study.moum.auth.domain.entity.MemberEntity;
import study.moum.community.article.domain.ArticleEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ArticleDto {

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Request{
        private int id;

        @NotEmpty @NotNull
        private String title;

        // ArticleDetails로 빼서 저장해줄거임 request.dto에만 존재
        private String content;

        private MemberEntity author;

        public ArticleEntity toEntity(){
            return ArticleEntity.builder()
                    .id(id)
                    .title(title)
                    .author(author)
                    .build();
        }
    }

    @Getter
    public static class Response{
        private final int id;
        private final String title;
        private final int viewCounts;
        private final int commentCounts;
        private final int likeCounts;
        private final String author;

        public Response(ArticleEntity article){
            this.id = article.getId();
            this.title = article.getTitle();
            this.author = article.getAuthor().getUsername();
            this.viewCounts = article.getViewCount();
            this.commentCounts = article.getCommentCount();
            this.likeCounts = article.getLikesCount();
        }
    }

}
