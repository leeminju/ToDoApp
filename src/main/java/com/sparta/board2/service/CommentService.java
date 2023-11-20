package com.sparta.board2.service;

import com.sparta.board2.dto.CommentRequestDto;
import com.sparta.board2.dto.CommentResponseDto;
import com.sparta.board2.entity.Comment;
import com.sparta.board2.entity.Todo;
import com.sparta.board2.entity.User;
import com.sparta.board2.repository.CommentRepository;
import com.sparta.board2.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final TodoRepository todoRepository;

    public ResponseEntity<CommentResponseDto> createComment(Long post_id, CommentRequestDto requestDto, User user) {
        Todo todo = todoRepository.findById(post_id).orElseThrow(
                () -> new NullPointerException("존재하지 않습니다")
        );

        Comment comment = commentRepository.save(new Comment(requestDto, user, todo));
        System.out.println("ABC");

        return ResponseEntity.status(HttpStatus.OK).body(new CommentResponseDto(comment));
    }

    public List<CommentResponseDto> getComments(Long post_id) {
        Todo todo = todoRepository.findById(post_id).orElseThrow(
                () -> new NullPointerException("존재하지 않습니다")
        );

        List<Comment> commentList = commentRepository.findByTodoOrderByCreatedAtDesc(todo);//댓글 작성일 기준 내림차순 정렬
        List<CommentResponseDto> responseDtoList = new ArrayList<>();

        for (Comment comment : commentList) {
            responseDtoList.add(new CommentResponseDto(comment));
        }

        return responseDtoList;
    }

    @Transactional
    public ResponseEntity<?> updateComment(Long comment_id, CommentRequestDto requestDto, User user) {
        Comment comment = commentRepository.findById(comment_id).orElseThrow(
                () -> new NullPointerException("해당 댓글 존재하지 않습니다")
        );

        if (!comment.getUser().getUsername().equals(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("댓글 작성자만 수정할 수 있습니다.");
        }

        comment.update(requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(new CommentResponseDto(comment));
    }

    public ResponseEntity<?> deleteComment(Long comment_id, User user) {
        Comment comment = commentRepository.findById(comment_id).orElseThrow(
                () -> new NullPointerException("해당 댓글 존재하지 않습니다")
        );

        if (!comment.getUser().getUsername().equals(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("댓글 작성자만  삭제할 수 있습니다.");
        }

        commentRepository.delete(comment);

        return ResponseEntity.status(HttpStatus.OK).body(comment_id+"번 댓글 삭제 성공");
    }
}
