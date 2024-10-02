package study.moum.community.article.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class ArticleDto {

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Request{
//        private int id;
//
//        @NotEmpty
//        @NotNull
//        @Size(min=3, max=10)
//        private String username;
//
//        @NotEmpty @NotNull
//        private String password;
//
//        @NotEmpty @NotNull @Email
//        private String email;
//
//        // private String verifyCode;
//
//        public MemberEntity toEntity(){
//            return MemberEntity.builder()
//                    .id(id)
//                    .username(username)
//                    .email(email)
//                    .password(password)
//                    .build();
//        }
    }

    @Getter
    public static class Response{
//        private final int id;
//        private final String username;
//
//        public Response(MemberEntity member){
//            this.id = member.getId();
//            this.username = member.getUsername();
//        }
    }

}
