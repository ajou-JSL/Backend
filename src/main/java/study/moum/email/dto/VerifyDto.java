package study.moum.email.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class VerifyDto {

    @NotNull @NotEmpty
    private String verifyCode;
}
