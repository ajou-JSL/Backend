package study.moum.email.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

public class EmailDto {

    @Getter
    @Setter
    public static class Request{
        @Email
        @NotEmpty(message = "이메일 입력을 입력하세요")
        private String email;
    }
}
