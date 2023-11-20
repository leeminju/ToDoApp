package com.sparta.board2.controller;

import com.sparta.board2.dto.CommentRequestDto;
import com.sparta.board2.dto.CommentResponseDto;
import com.sparta.board2.security.UserDetailsImpl;
import com.sparta.board2.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{post_id}")
    public ResponseEntity<?> createComment(@PathVariable Long post_id, @RequestBody @Valid CommentRequestDto requestDto, BindingResult bindingResult, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // Validation 예외처리
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if (!fieldErrors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("내용을 입력해주세요!!");
        }

        return commentService.createComment(post_id, requestDto, userDetails.getUser());
    }

    @GetMapping("/{post_id}")
    public List<CommentResponseDto> getComments(@PathVariable Long post_id) {
        return commentService.getComments(post_id);
    }

    @PutMapping("/{comment_id}")
    public ResponseEntity<?> updateComment(@PathVariable Long comment_id, @RequestBody @Valid CommentRequestDto requestDto, BindingResult bindingResult, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // Validation 예외처리
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if (!fieldErrors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("내용을 입력해주세요!!");
        }

        return commentService.updateComment(comment_id, requestDto, userDetails.getUser());
    }

    @DeleteMapping("/{comment_id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long comment_id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.deleteComment(comment_id, userDetails.getUser());
    }
}
