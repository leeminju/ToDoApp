package com.sparta.board2.service;

import com.sparta.board2.dto.CommentRequestDto;
import com.sparta.board2.dto.CommentResponseDto;
import com.sparta.board2.entity.Comment;
import com.sparta.board2.entity.Todo;
import com.sparta.board2.entity.User;
import com.sparta.board2.repository.CommentRepository;
import com.sparta.board2.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final TodoRepository todoRepository;

    public CommentResponseDto createComment(Long postId, CommentRequestDto requestDto, User user) {
        Todo todo = todoRepository.findById(postId).orElseThrow(
                () -> new NullPointerException("할일이 존재하지 않습니다")
        );

        Comment comment = commentRepository.save(new Comment(requestDto, user, todo));

        return new CommentResponseDto(comment);
    }

    public List<CommentResponseDto> getComments(Long postId) {
        Todo todo = todoRepository.findById(postId).orElseThrow(
                () -> new NullPointerException("할일이 존재하지 않습니다")
        );

        List<Comment> commentList = commentRepository.findByTodoOrderByCreatedAtDesc(todo);//댓글 작성일 기준 내림차순 정렬
        List<CommentResponseDto> responseDtoList = new ArrayList<>();

        for (Comment comment : commentList) {
            responseDtoList.add(new CommentResponseDto(comment));
        }

        return responseDtoList;
    }

    public CommentResponseDto getComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new NullPointerException("해당 댓글 존재하지 않습니다")
        );

        return new CommentResponseDto(comment);
    }

    @Transactional
    public CommentResponseDto updateComment(Long commentId, CommentRequestDto requestDto, User user) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new NullPointerException("해당 댓글 존재하지 않습니다")
        );

        if (!comment.getUser().getUsername().equals(user.getUsername())) {
            throw new IllegalStateException("댓글 작성자만 수정할 수 있습니다.");
        }

        comment.update(requestDto);

        return new CommentResponseDto(comment);
    }

    public void deleteComment(Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new NullPointerException("해당 댓글 존재하지 않습니다")
        );

        if (!comment.getUser().getUsername().equals(user.getUsername())) {
            throw new IllegalStateException("댓글 작성자만 삭제할 수 있습니다.");
        }

        commentRepository.delete(comment);
    }


}
