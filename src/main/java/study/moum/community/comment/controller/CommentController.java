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

    /**
     * 댓글 생성 API 엔드포인트.
     *
     * @param customUserDetails 현재 인증된 사용자 정보 (CustomUserDetails 객체에서 사용자 정보 추출)
     * @param id 댓글을 달 게시글의 ID (경로 변수)
     * @param commentRequestDto 요청으로 들어온 댓글의 내용이 담긴 DTO (유효성 검증 적용)
     * @return 댓글 생성 성공 메시지와 생성된 댓글 정보를 담은 응답 객체
     *
     * 해당 엔드포인트는 사용자 인증 정보를 받아 특정 게시글에 댓글을 작성하는 역할을 한다.
     *
     * - 사용자 인증 정보를 `@AuthenticationPrincipal`을 통해 받아 작성자의 정보를 확인
     * - `@PathVariable`로 댓글이 달릴 게시글의 ID를 받아 해당 게시글과 관련된 작업을 수행
     * - `@RequestBody`로 들어온 댓글 내용에 대해 유효성 검사를 하고, DTO에서 엔티티로 변환하여 저장
     * - `commentService.createComment`를 호출하여 댓글 생성 로직을 처리
     * - 댓글 생성이 성공하면 성공 코드와 함께 `ResultResponse` 객체로 응답
     */
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