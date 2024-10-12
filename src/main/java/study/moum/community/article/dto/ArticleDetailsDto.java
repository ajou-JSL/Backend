package study.moum.community.article.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import study.moum.auth.domain.entity.MemberEntity;
import study.moum.community.article.domain.ArticleCategories;
import study.moum.community.article.domain.ArticleDetailsEntity;
import study.moum.community.article.domain.ArticleEntity;
import study.moum.community.comment.dto.CommentDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ArticleDetailsDto {

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Request{
        private int id;

        private ArticleCategories category;

        // only update request 용도
        private String title;

        // ArticleDetails로 빼서 저장해줄거임 request.dto에만 존재
        private String content;

        public ArticleDetailsEntity toEntity(){
            return ArticleDetailsEntity.builder()
                    .id(id)
                    .content(content)
                    .build();
        }
    }

    @Getter
    public static class Response{
        private final int id;
        private final String title;
        private final String category;
        private final String content;
        private final int viewCounts;
        private final int commentCounts;
        private final int likeCounts;
        private final String author;
        private List<CommentDto.Response> comments = new ArrayList<>();

        public Response(ArticleDetailsEntity articleDetails, ArticleEntity article){
            this.id = article.getId();
            this.title = article.getTitle();
            this.category = article.getCategory().toString();
            this.author = article.getAuthor().getUsername();
            this.viewCounts = article.getViewCount();
            this.commentCounts = article.getCommentCount();
            this.likeCounts = article.getLikesCount();
            this.comments = articleDetails.getComments().stream()
                    .map(CommentDto.Response::new)
                    .collect(Collectors.toList());
            this.content = articleDetails.getContent();
        }
    }

}
