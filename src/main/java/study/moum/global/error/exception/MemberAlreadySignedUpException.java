package study.moum.global.error.exception;


import study.moum.global.error.ErrorCode;

public class MemberAlreadySignedUpException extends CustomException{
    public MemberAlreadySignedUpException(){super(ErrorCode.MEMBER_ALREADY_EXISTS);}
}
