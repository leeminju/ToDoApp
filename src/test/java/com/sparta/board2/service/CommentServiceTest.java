package com.sparta.board2.service;

import com.sparta.board2.comment.*;
import com.sparta.board2.global.exception.RestApiException;
import com.sparta.board2.todo.Todo;
import com.sparta.board2.todo.TodoRepository;
import com.sparta.board2.todo.TodoRequestDto;
import com.sparta.board2.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    CommentRepository commentRepository;

    @Mock
    TodoRepository todoRepository;
    Long post_id;
    Long comment_id;
    User commentWriter;
    User loginedUser;
    TodoRequestDto todoRequestDto;
    User todoWriter;
    CommentRequestDto createRequestDto;
    CommentRequestDto updateRequestDto;

    @BeforeEach
    void setUp() {
        post_id = 1L;
        comment_id = 1L;
        createRequestDto = new CommentRequestDto("test 댓글");
        updateRequestDto = new CommentRequestDto("댓글 수정");
        commentWriter = new User("tester1", "tester21234");
        loginedUser = new User("loginedUser", "password");
        todoRequestDto = new TodoRequestDto("할일 제목", "할일 내용");
        todoWriter = new User("tester", "tester1234");
    }

    @Test
    @DisplayName("댓글 생성 테스트 - Todo 존재 하지 않음")
    void createCommentTest() {
        //given
        CommentRequestDto createRequestDto = new CommentRequestDto("test 댓글");
        CommentService commentService = new CommentService(commentRepository, todoRepository);
        //when
        RestApiException exception = assertThrows(RestApiException.class, () -> {
            commentService.createComment(post_id, createRequestDto, loginedUser);
        });
        //then
        assertEquals("할일이 존재하지 않습니다.", exception.getErrorMessage());
    }

    @Test
    @DisplayName("댓글 생성 테스트 - 성공")
    void createCommentTest2() {
        //given
        Todo todo = new Todo(todoRequestDto, todoWriter);
        //게시글 아이디로 찾을 경우 테스트 케이스에서 생성한 객체를 리턴
        given(todoRepository.findById(post_id)).willReturn(Optional.of(todo));
        Comment comment = new Comment(createRequestDto, commentWriter, todo);

        //댓글 입력된 경우 테스트 케이스에서 생성한 객체를 리턴
        given(commentRepository.save(any(Comment.class))).willReturn(comment);
        CommentService commentService = new CommentService(commentRepository, todoRepository);
        //when
        CommentResponseDto commentResponseDto = commentService.createComment(post_id, createRequestDto, commentWriter);

        //then
        assertEquals(createRequestDto.getContents(), commentResponseDto.getContents());
        assertEquals(commentWriter.getUsername(), commentResponseDto.getUsername());
    }

    @Test
    @DisplayName("Todo 내 댓글 전체 조회 테스트 - Todo 존재하지 않음")
    void getCommentsTest() {
        //given
        CommentService commentService = new CommentService(commentRepository, todoRepository);

        //when
        RestApiException exception = assertThrows(RestApiException.class, () -> {
            commentService.getComments(post_id);
        });
        //then
        assertEquals("할일이 존재하지 않습니다.", exception.getErrorMessage());
    }

    @Test
    @DisplayName("Todo 내 댓글 전체 조회 테스트 - 성공")
    void getCommentsTest2() {
        //given
        User commentWriter1 = new User("tester1", "tester21234");
        User commentWriter2 = new User("tester2", "tester21234");
        Todo todo = new Todo(todoRequestDto, todoWriter);

        List<Comment> commentList = new ArrayList<>();
        commentList.add(new Comment(new CommentRequestDto("댓글1"), commentWriter1, todo));
        commentList.add(new Comment(new CommentRequestDto("댓글2"), commentWriter2, todo));
        given(todoRepository.findById(post_id)).willReturn(Optional.of(todo));
        given(commentRepository.findByTodoOrderByCreatedAtDesc(todo)).willReturn(commentList);

        CommentService commentService = new CommentService(commentRepository, todoRepository);
        //when
        List<CommentResponseDto> responseDtoList = commentService.getComments(post_id);

        //then
        assertEquals(commentList.size(), responseDtoList.size());
        assertEquals("댓글1", responseDtoList.get(0).getContents());
        assertEquals("tester1", responseDtoList.get(0).getUsername());

        assertEquals("댓글2", responseDtoList.get(1).getContents());
        assertEquals("tester2", responseDtoList.get(1).getUsername());
    }


    @Test
    @DisplayName("댓글 조회 테스트 - 댓글 존재하지 않음")
    void getCommentTest() {
        //given
        CommentService commentService = new CommentService(commentRepository, todoRepository);

        //when
        RestApiException exception = assertThrows(RestApiException.class, () -> {
            commentService.getComment(comment_id);
        });
        //then
        assertEquals("해당 댓글 존재하지 않습니다.", exception.getErrorMessage());
    }

    @Test
    @DisplayName("댓글 조회 테스트 - 댓글 조회 성공")
    void getCommentTest2() {
        //given
        Todo todo = new Todo(todoRequestDto, todoWriter);
        Comment comment = new Comment(createRequestDto, commentWriter, todo);

        given(commentRepository.findById(comment_id)).willReturn(Optional.of(comment));

        CommentService commentService = new CommentService(commentRepository, todoRepository);

        //when
        CommentResponseDto commentResponseDto = commentService.getComment(comment_id);

        //then
        assertEquals("test 댓글", commentResponseDto.getContents());
        assertEquals("tester1", commentResponseDto.getUsername());
    }


    @Test
    @DisplayName("댓글 수정 테스트 - 댓글 존재 하지 않음")
    void updateCommentTest() {
        //given
        CommentService commentService = new CommentService(commentRepository, todoRepository);

        //when
        RestApiException exception = assertThrows(RestApiException.class, () -> {
            commentService.updateComment(comment_id, updateRequestDto, loginedUser);
        });
        //then
        assertEquals("해당 댓글 존재하지 않습니다.", exception.getErrorMessage());
    }

    @Test
    @DisplayName("댓글 수정 테스트 - 댓글 작성자 아님")
    void updateCommentTest2() {
        //given
        Todo todo = new Todo(todoRequestDto, todoWriter);
        Comment comment = new Comment(createRequestDto, commentWriter, todo);
        given(commentRepository.findById(comment_id)).willReturn(Optional.of(comment));

        CommentService commentService = new CommentService(commentRepository, todoRepository);
        //when
        RestApiException exception = assertThrows(RestApiException.class, () -> {
            commentService.updateComment(comment_id, updateRequestDto, loginedUser);
        });
        //then
        assertEquals("댓글 작성자만 수정할 수 있습니다.", exception.getErrorMessage());
    }

    @Test
    @DisplayName("댓글 수정 테스트 - 댓글 수정 성공")
    void updateCommentTest3() {
        //given
        User commentWriter = new User("loginedUser", "password");
        Todo todo = new Todo(todoRequestDto, todoWriter);

        Comment comment = new Comment(createRequestDto, commentWriter, todo);
        given(commentRepository.findById(comment_id)).willReturn(Optional.of(comment));

        CommentService commentService = new CommentService(commentRepository, todoRepository);
        //when
        CommentResponseDto commentResponseDto = commentService.updateComment(comment_id, updateRequestDto, loginedUser);

        //then
        assertEquals("댓글 수정", commentResponseDto.getContents());
    }

    @Test
    @DisplayName("댓글 삭제 테스트 - 댓글 존재 하지 않음")
    void deleteCommentTest() {
        //given
        User user = new User("tester", "password");
        CommentService commentService = new CommentService(commentRepository, todoRepository);

        //when
        RestApiException exception = assertThrows(RestApiException.class, () -> {
            commentService.deleteComment(comment_id, user);
        });
        //then
        assertEquals("해당 댓글 존재하지 않습니다.", exception.getErrorMessage());
    }

    @Test
    @DisplayName("댓글 삭제 테스트 - 댓글 작성자 아님")
    void deleteCommentTest2() {
        //given
        Todo todo = new Todo(todoRequestDto, todoWriter);
        Comment comment = new Comment(createRequestDto, commentWriter, todo);
        given(commentRepository.findById(comment_id)).willReturn(Optional.of(comment));

        CommentService commentService = new CommentService(commentRepository, todoRepository);
        //when
        RestApiException exception = assertThrows(RestApiException.class, () -> {
            commentService.deleteComment(comment_id, loginedUser);
        });
        //then
        assertEquals("댓글 작성자만 삭제할 수 있습니다.", exception.getErrorMessage());
    }

    @Test
    @DisplayName("댓글 삭제 테스트 - 댓글 삭제 성공")
    void deleteCommentTest3() {
        //given
        User commentWriter = new User("loginedUser", "password");
        Todo todo = new Todo(todoRequestDto, todoWriter);

        Comment comment = new Comment(createRequestDto, commentWriter, todo);
        given(commentRepository.findById(comment_id)).willReturn(Optional.of(comment));
        CommentService commentService = new CommentService(commentRepository, todoRepository);
        //when
        commentService.deleteComment(comment_id, loginedUser);

        //then
        verify(commentRepository).delete(any(Comment.class));
    }

}