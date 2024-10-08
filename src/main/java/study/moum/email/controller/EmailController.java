package study.moum.email.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import study.moum.email.dto.EmailDto;
import study.moum.email.service.EmailService;
import study.moum.global.response.ResponseCode;
import study.moum.global.response.ResultResponse;

@RestController
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;

    @PostMapping("/send-mail")
    public ResponseEntity<?> emailAuthentification (@RequestBody EmailDto.Request emailRequestDto) throws Exception {
        String verifyCode = emailService.sendCertificationMail(emailRequestDto.getEmail());

        ResultResponse resultResponse = ResultResponse.of(ResponseCode.EMAIL_SEND_SUCCESS, /*verifyCode*/emailRequestDto.getEmail());
        return new ResponseEntity<>(resultResponse, HttpStatus.valueOf(resultResponse.getStatus()));
    }
}
