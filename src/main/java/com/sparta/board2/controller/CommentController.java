package com.sparta.board2.controller;

import com.sparta.board2.dto.CommentRequestDto;
import com.sparta.board2.dto.CommentResponseDto;
import com.sparta.board2.response.CustomResponseEntity;
import com.sparta.board2.security.UserDetailsImpl;
import com.sparta.board2.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    //댓글 작성
    @PostMapping("/post/{post_id}/comment")
    public ResponseEntity<CustomResponseEntity> createComment(@PathVariable Long post_id, @RequestBody @Valid CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.createComment(post_id, requestDto, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CustomResponseEntity(
                                post_id + "번 할일 댓글 작성 완료",
                                HttpStatus.CREATED.value()
                        )
                );
    }

    //게시글에 해당되는 댓글 모두 보기
    @GetMapping("/post/{post_id}/comments")
    public List<CommentResponseDto> getComments(@PathVariable Long post_id) {
        return commentService.getComments(post_id);
    }

    //댓글 1개 보기
    @GetMapping("/comment/{comment_id}")
    public CommentResponseDto getComment(@PathVariable Long comment_id) {
        return commentService.getComment(comment_id);
    }

    //댓글 수정
    @PutMapping("/comment/{comment_id}")
    public ResponseEntity<CustomResponseEntity> updateComment(@PathVariable Long comment_id, @RequestBody @Valid CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.updateComment(comment_id, requestDto, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomResponseEntity(
                        "댓글 수정 완료",
                        HttpStatus.OK.value()
                )
        );
    }

    //댓글 삭제
    @DeleteMapping("/comment/{comment_id}")
    public ResponseEntity<CustomResponseEntity> deleteComment(@PathVariable Long comment_id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.deleteComment(comment_id, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomResponseEntity(
                        "댓글 삭제 완료",
                        HttpStatus.OK.value()
                )
        );
    }
}
