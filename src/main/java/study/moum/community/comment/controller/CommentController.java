package study.moum.community.comment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import study.moum.auth.domain.CustomUserDetails;
import study.moum.community.article.dto.ArticleDetailsDto;
import study.moum.community.comment.dto.CommentDto;
import study.moum.community.comment.service.CommentService;
import study.moum.global.response.ResponseCode;
import study.moum.global.response.ResultResponse;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/api/comments/{id}")
    public ResponseEntity<ResultResponse> createComment(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable int id,
            @Valid @RequestBody CommentDto.Request commentRequestDto)
    {
        CommentDto.Response commentResponse = commentService.createComment(commentRequestDto, customUserDetails.getUsername(), id);
        ResultResponse response = ResultResponse.of(ResponseCode.COMMENT_CREATE_SUCCESS, commentResponse);

        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }
}