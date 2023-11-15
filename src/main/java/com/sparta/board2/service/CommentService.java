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

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final TodoRepository todoRepository;

    public CommentResponseDto createComment(Long post_id, CommentRequestDto requestDto, User user) {
        Todo todo = todoRepository.findById(post_id).orElseThrow(
                () -> new NullPointerException("존재하지 않습니다")
        );

        Comment comment = commentRepository.save(new Comment(requestDto, user, todo));
        System.out.println("ABC");

        return new CommentResponseDto(comment);
    }

    public List<CommentResponseDto> getComments(Long post_id) {
        Todo todo = todoRepository.findById(post_id).orElseThrow(
                () -> new NullPointerException("존재하지 않습니다")
        );
        List<Comment> commentList = todo.getComment();
        List<CommentResponseDto> responseDtoList = new ArrayList<>();

        for (Comment comment : commentList) {
            responseDtoList.add(new CommentResponseDto(comment));
        }

        return responseDtoList;
    }
}
