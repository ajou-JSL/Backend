package study.moum.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import study.moum.auth.domain.CustomUserDetails;
import study.moum.global.response.ResultResponse;

@RestController
public class TestController {

    @GetMapping("/test")
    public String testPage(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        // System.out.println(customUserDetails.getUsername());

//        return new ResponseEntity<>(HttpStatus.OK);
        return "test";
    }
}
