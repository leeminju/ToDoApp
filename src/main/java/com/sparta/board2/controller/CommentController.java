package com.sparta.board2.controller;

import com.sparta.board2.dto.CommentRequestDto;
import com.sparta.board2.dto.CommentResponseDto;
import com.sparta.board2.dto.TodoRequestDto;
import com.sparta.board2.security.UserDetailsImpl;
import com.sparta.board2.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comment/{post_id}")
    public CommentResponseDto createComment(@PathVariable Long post_id, @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.createComment(post_id, requestDto, userDetails.getUser());
    }

    @GetMapping("/comment/{post_id}")
    public List<CommentResponseDto> getComments(@PathVariable Long post_id) {
        return commentService.getComments(post_id);
    }


}