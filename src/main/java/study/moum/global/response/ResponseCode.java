package study.moum.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCode {

    // Member
    REGISTER_SUCCESS(200, "M001", "회원가입 되었습니다."),
    LOGIN_SUCCESS(200, "M002", "로그인 되었습니다."),
    REISSUE_SUCCESS(200, "M003", "재발급 되었습니다."),
    LOGOUT_SUCCESS(200, "M004", "로그아웃 되었습니다."),
    GET_MY_INFO_SUCCESS(200, "M005", "내 정보 조회 완료"),

    // Article
    ARTICLE_LIST_GET_SUCCESS(200,"A001","게시글 목록 조회 성공."),
    ARTICLE_GET_SUCCESS(200,"A002","게시글 단건 조회 성공."),
    ARTICLE_POST_SUCCESS(201,"A003","게시글 등록 성공."),
    ARTICLE_UPDATE_SUCCESS(201,"A003","게시글 수정 성공."),
    ARTICLE_DELETE_SUCCESS(200,"A003","게시글 삭제 성공."),

    // Jwt
    ACCESS_TOKEN(200, "J001", "액세스 토큰 발급 성공"),

    // Email
    EMAIL_SEND_SUCCESS(200,"E001","인증 이메일 발송 성공하였습니다."),
    EMAIL_VERIFY_SUCCESS(200,"E001","이메일 인증 성공하였습니다."),

    // Commenet
    COMMENT_CREATE_SUCCESS(201,"C001","댓글 작성 성공"),
    COMMENT_UPDATE_SUCCESS(201,"C002","댓글 수정 성공"),
    COMMENT_DELETE_SUCCESS(200,"C003","댓글 삭제 성공"),

    // Likes
    LIKES_CREATE_SUCCESS(201,"L001","좋아요 등록 성공"),
    LIKES_DELETE_SUCCESS(200,"L002","좋아요 삭제 성공");


    private final int status;
    private final String code;
    private final String message;
}