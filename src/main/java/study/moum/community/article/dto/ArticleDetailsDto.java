package study.moum.community.article.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import study.moum.auth.domain.entity.MemberEntity;
import study.moum.community.article.domain.ArticleDetailsEntity;
import study.moum.community.article.domain.ArticleEntity;

public class ArticleDetailsDto {
//
//    @Builder
//    @AllArgsConstructor
//    @Getter
//    public static class Request{
//        private int id;
//
//        @NotEmpty @NotNull
//        private String title;
//
//        // ArticleDetails로 빼서 저장해줄거임 request.dto에만 존재
//        private String content;
//
//        private MemberEntity author;
//
//        public ArticleEntity toEntity(){
//            return ArticleEntity.builder()
//                    .id(id)
//                    .title(title)
//                    .author(author)
//                    .build();
//        }
//    }

    @Getter
    public static class Response{
//        private final int id;
//        private final String title;
//        private final String content;
//        private final int viewCounts;
//        private final int commentCounts;
//        private final int likeCounts;
//        private final String author;
        // private List<CommentDto.Response> comments = new ArrayList<>();

        public Response(ArticleDetailsEntity articleDetails, ArticleEntity article){
//            this.id = article.getId();
//            this.content = article.getContent();
//            this.author = article.getAuthor().toString();
//            this.viewCounts = article.getViewCount();
//            this.commentCounts = article.getCommentCount();
//            this.likeCounts = article.getLikesCount();
//            this.comments = article.getComments().stream()
//                    .map(CommentDto.Response::new)
//                    .collect(Collectors.toList());
        }
    }

}
