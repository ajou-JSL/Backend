package study.moum.email.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class EmailDto {

    @Getter
    @Setter
    public static class Request{
        @Email(message = "올바른 이메일 형식이 아닙니다")
        @NotEmpty(message = "이메일 입력을 입력하세요")
        private String email;
    }
}
