package study.moum.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // Common
    INTERNAL_SERVER_ERROR(500, "C001", "internal server error"),
    INVALID_INPUT_VALUE(400, "C002", "invalid input type"),
    METHOD_NOT_ALLOWED(405, "C003", "method not allowed"),
    INVALID_TYPE_VALUE(400, "C004", "invalid type value"),
    BAD_CREDENTIALS(400, "C005", "bad credentials"),

    // Member
    MEMBER_NOT_EXIST(404, "M001", "존재하지 않은 회원입니다."),
    USER_NAME_ALREADY_EXISTS(409, "M002", "이미 존재하는 아이디입니다."),
    NO_AUTHORITY(403, "M003", "권한이 없습니다."),
    NEED_LOGIN(401, "M004", "로그인을 해야합니다."),
    AUTHENTICATION_NOT_FOUND(401, "M005", "인증되지 않은 회원입니다."),
    MEMBER_ALREADY_LOGOUT(400, "M006", "이미 로그아웃 하였습니다."),

    // Auth
    REFRESH_TOKEN_INVALID(400, "A001", "유효하지 않은 refresh 토큰 입니다."),
    JWT_TOKEN_EXPIRED(401, "A002", "jwt 토큰이 만료되었습니다."),

    EMAIL_VERIFY_FAILED(400,"E001","이메일 인증이 실패하였습니다."),

    // Article
    ARTICLE_NOT_FOUND(404,"AT001","게시글을 찾을 수 없습니다."),
    ARTICLE_ALREADY_DELETED(404,"AT002","이미 삭제된 게시글입니다."),

    // Comment
    COMMENT_NOT_FOUND(404,"CM001","댓글을 찾을 수 없습니다."),
    COMMENT_ALREADY_DELETED(404,"CM002","이미 삭제된 댓글입니다.");

    private final int status;
    private final String code;
    private final String message;
}