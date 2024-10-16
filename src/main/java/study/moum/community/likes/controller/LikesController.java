package study.moum.community.likes.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import study.moum.auth.domain.CustomUserDetails;
import study.moum.community.article.dto.ArticleDto;
import study.moum.community.likes.dto.LikesDto;
import study.moum.community.likes.service.LikesService;
import study.moum.global.error.exception.NeedLoginException;
import study.moum.global.response.ResponseCode;
import study.moum.global.response.ResultResponse;

@RestController
@RequiredArgsConstructor
public class LikesController {

    private final LikesService likesService;

    @PostMapping("/api/likes/{articleId}")
    public ResponseEntity<ResultResponse> createLikes(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                      @PathVariable int articleId){
        if(customUserDetails == null){
            throw new NeedLoginException();
        }
        LikesDto.Response likesResponse = likesService.createLikes(customUserDetails.getUsername(),articleId);

        ResultResponse response = ResultResponse.of(ResponseCode.LIKES_CREATE_SUCCESS, likesResponse);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }
}
