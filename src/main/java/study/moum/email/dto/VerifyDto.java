package study.moum.email.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class VerifyDto {

    @NotNull @NotEmpty(message = "인증 코드를 입력해야합니다.")
    private String verifyCode;
}
