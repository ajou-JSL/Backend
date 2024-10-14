package study.moum.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import study.moum.auth.dto.MemberDto;
import study.moum.auth.service.SignupService;
import study.moum.global.response.ResponseCode;
import study.moum.global.response.ResultResponse;

@RestController
@RequiredArgsConstructor
public class SignupController {
    private final SignupService signupService;

    @PostMapping("/signup")
    public ResponseEntity<ResultResponse> signupMember(@Valid @RequestBody MemberDto.Request memberRequestDto) {

        signupService.signupMember(memberRequestDto);
        ResultResponse resultResponse = ResultResponse.of(ResponseCode.REGISTER_SUCCESS, memberRequestDto.getUsername());

        return new ResponseEntity<>(resultResponse, HttpStatus.valueOf(resultResponse.getStatus()));
    }
}
