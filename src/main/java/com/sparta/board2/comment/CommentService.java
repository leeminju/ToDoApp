package com.sparta.board2.comment;

import com.sparta.board2.global.exception.ErrorCode;
import com.sparta.board2.global.exception.RestApiException;
import com.sparta.board2.todo.Todo;
import com.sparta.board2.user.User;
import com.sparta.board2.todo.TodoRepository;
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
                () -> new RestApiException(ErrorCode.NOT_FOUND_TODO)
        );

        Comment comment = commentRepository.save(new Comment(requestDto, user, todo));

        return new CommentResponseDto(comment);
    }

    public List<CommentResponseDto> getComments(Long postId) {
        Todo todo = todoRepository.findById(postId).orElseThrow(
                () -> new RestApiException(ErrorCode.NOT_FOUND_TODO)
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
                () -> new RestApiException(ErrorCode.NOT_FOUND_COMMENT)
        );

        return new CommentResponseDto(comment);
    }

    @Transactional
    public CommentResponseDto updateComment(Long commentId, CommentRequestDto requestDto, User user) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new RestApiException(ErrorCode.NOT_FOUND_COMMENT)
        );

        if (!comment.getUser().getUsername().equals(user.getUsername())) {
            throw new RestApiException(ErrorCode.CAN_NOT_MODIFY_COMMENT);
        }

        comment.update(requestDto);

        return new CommentResponseDto(comment);
    }

    public void deleteComment(Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new RestApiException(ErrorCode.NOT_FOUND_COMMENT)
        );

        if (!comment.getUser().getUsername().equals(user.getUsername())) {
            throw new RestApiException(ErrorCode.CAN_NOT_DELETE_COMMENT);
        }

        commentRepository.delete(comment);
    }


}
