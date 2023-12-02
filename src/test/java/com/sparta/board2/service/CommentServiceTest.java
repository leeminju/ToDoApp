package com.sparta.board2.service;

import com.sparta.board2.dto.CommentRequestDto;
import com.sparta.board2.dto.CommentResponseDto;
import com.sparta.board2.dto.TodoRequestDto;
import com.sparta.board2.entity.Comment;
import com.sparta.board2.entity.Todo;
import com.sparta.board2.entity.User;
import com.sparta.board2.repository.CommentRepository;
import com.sparta.board2.repository.TodoRepository;
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


    @Test
    @DisplayName("댓글 생성 테스트 - Todo 존재 하지 않음")
    void createCommentTest() {
        //given
        Long post_id = 1L;
        CommentRequestDto commentRequestDto = new CommentRequestDto("test 댓글");
        User user = new User();

        CommentService commentService = new CommentService(commentRepository, todoRepository);

        //when
        Exception exception = assertThrows(NullPointerException.class, () -> {
            commentService.createComment(post_id, commentRequestDto, user);
        });
        //then
        assertEquals("할일이 존재하지 않습니다", exception.getMessage());
    }

    @Test
    @DisplayName("댓글 생성 테스트 - 성공")
    void createCommentTest2() {
        //given
        Long post_id = 1L;
        CommentRequestDto commentRequestDto = new CommentRequestDto("test 댓글");
        User todoWriter = new User("tester", "tester1234");
        User commentWriter = new User("tester2", "tester21234");
        TodoRequestDto todoRequestDto = new TodoRequestDto("할일 제목", "할일 내용");
        Todo todo = new Todo(todoRequestDto, todoWriter);
        //게시글 아이디로 찾을 경우 테스트 케이스에서 생성한 객체를 리턴
        given(todoRepository.findById(post_id)).willReturn(Optional.of(todo));
        Comment comment = new Comment(commentRequestDto, commentWriter, todo);

        //댓글 입력된 경우 테스트 케이스에서 생성한 객체를 리턴
        given(commentRepository.save(any(Comment.class))).willReturn(comment);
        CommentService commentService = new CommentService(commentRepository, todoRepository);
        //when
        CommentResponseDto commentResponseDto = commentService.createComment(post_id, commentRequestDto, commentWriter);

        //then
        assertEquals(commentRequestDto.getContents(), commentResponseDto.getContents());
        assertEquals(commentWriter.getUsername(), commentResponseDto.getUsername());
    }

    @Test
    @DisplayName("Todo 내 댓글 전체 조회 테스트 - Todo 존재하지 않음")
    void getCommentsTest() {
        //given
        Long post_id = 1L;
        CommentService commentService = new CommentService(commentRepository, todoRepository);

        //when
        Exception exception = assertThrows(NullPointerException.class, () -> {
            commentService.getComments(post_id);
        });
        //then
        assertEquals("할일이 존재하지 않습니다", exception.getMessage());
    }

    @Test
    @DisplayName("Todo 내 댓글 전체 조회 테스트 - 성공")
    void getCommentsTest2() {
        //given
        Long post_id = 1L;

        TodoRequestDto todoRequestDto = new TodoRequestDto("할일 제목", "할일 내용");
        User todoWriter = new User("tester", "tester1234");

        User commentWriter1 = new User("tester1", "tester21234");
        User commentWriter2 = new User("tester2", "tester21234");
        Todo todo = new Todo(todoRequestDto, todoWriter);
        //게시글 아이디로 찾을 경우 테스트 케이스에서 생성한 객체를 리턴

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
        Long comment_id = 1L;
        CommentService commentService = new CommentService(commentRepository, todoRepository);

        //when
        Exception exception = assertThrows(NullPointerException.class, () -> {
            commentService.getComment(comment_id);
        });
        //then
        assertEquals("해당 댓글 존재하지 않습니다", exception.getMessage());
    }

    @Test
    @DisplayName("댓글 조회 테스트 - 댓글 조회 성공")
    void getCommentTest2() {
        //given
        Long comment_id = 1L;
        User commentWriter1 = new User("tester1", "tester21234");

        TodoRequestDto todoRequestDto = new TodoRequestDto("할일 제목", "할일 내용");
        User todoWriter = new User("tester", "tester1234");
        Todo todo = new Todo(todoRequestDto, todoWriter);
        Comment comment = new Comment(new CommentRequestDto("댓글 테스트"), commentWriter1, todo);

        given(commentRepository.findById(comment_id)).willReturn(Optional.of(comment));

        CommentService commentService = new CommentService(commentRepository, todoRepository);

        //when
        CommentResponseDto commentResponseDto = commentService.getComment(comment_id);

        //then
        assertEquals("댓글 테스트", commentResponseDto.getContents());
        assertEquals("tester1", commentResponseDto.getUsername());
    }


    @Test
    @DisplayName("댓글 수정 테스트 - 댓글 존재 하지 않음")
    void updateCommentTest() {
        //given
        Long comment_id = 1L;
        CommentRequestDto requestDto = new CommentRequestDto("댓글 수정");
        User user = new User("tester", "password");
        CommentService commentService = new CommentService(commentRepository, todoRepository);

        //when
        Exception exception = assertThrows(NullPointerException.class, () -> {
            commentService.updateComment(comment_id, requestDto, user);
        });
        //then
        assertEquals("해당 댓글 존재하지 않습니다", exception.getMessage());
    }

    @Test
    @DisplayName("댓글 수정 테스트 - 댓글 작성자 아님")
    void updateCommentTest2() {
        //given
        Long comment_id = 1L;
        CommentRequestDto requestDto = new CommentRequestDto("댓글 수정");
        User commentWriter = new User("tester", "password");
        TodoRequestDto todoRequestDto = new TodoRequestDto("할일 제목", "할일 내용");
        User todoWriter = new User("tester", "tester1234");
        Todo todo = new Todo(todoRequestDto, todoWriter);
        Comment comment = new Comment(new CommentRequestDto("댓글 원본"), commentWriter, todo);
        User loginedUser = new User("loginedUser", "password");
        given(commentRepository.findById(comment_id)).willReturn(Optional.of(comment));

        CommentService commentService = new CommentService(commentRepository, todoRepository);
        //when
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            commentService.updateComment(comment_id, requestDto, loginedUser);
        });
        //then
        assertEquals("댓글 작성자만 수정할 수 있습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("댓글 수정 테스트 - 댓글 수정 성공")
    void updateCommentTest3() {
        //given
        Long comment_id = 1L;
        CommentRequestDto requestDto = new CommentRequestDto("댓글 수정");
        User commentWriter = new User("loginedUser", "password");
        TodoRequestDto todoRequestDto = new TodoRequestDto("할일 제목", "할일 내용");
        User todoWriter = new User("tester", "tester1234");
        Todo todo = new Todo(todoRequestDto, todoWriter);

        Comment comment = new Comment(new CommentRequestDto("댓글 원본"), commentWriter, todo);
        User loginedUser = new User("loginedUser", "password");
        given(commentRepository.findById(comment_id)).willReturn(Optional.of(comment));

        CommentService commentService = new CommentService(commentRepository, todoRepository);
        //when
        CommentResponseDto commentResponseDto = commentService.updateComment(comment_id, requestDto, loginedUser);

        //then
        assertEquals("댓글 수정", commentResponseDto.getContents());
    }

    @Test
    @DisplayName("댓글 삭제 테스트 - 댓글 존재 하지 않음")
    void deleteCommentTest() {
        //given
        Long comment_id = 1L;
        User user = new User("tester", "password");
        CommentService commentService = new CommentService(commentRepository, todoRepository);

        //when
        Exception exception = assertThrows(NullPointerException.class, () -> {
            commentService.deleteComment(comment_id, user);
        });
        //then
        assertEquals("해당 댓글 존재하지 않습니다", exception.getMessage());
    }

    @Test
    @DisplayName("댓글 삭제 테스트 - 댓글 작성자 아님")
    void deleteCommentTest2() {
        //given
        Long comment_id = 1L;
        User commentWriter = new User("tester", "password");
        TodoRequestDto todoRequestDto = new TodoRequestDto("할일 제목", "할일 내용");
        User todoWriter = new User("tester", "tester1234");
        Todo todo = new Todo(todoRequestDto, todoWriter);
        Comment comment = new Comment(new CommentRequestDto("댓글 원본"), commentWriter, todo);
        User loginedUser = new User("loginedUser", "password");
        given(commentRepository.findById(comment_id)).willReturn(Optional.of(comment));

        CommentService commentService = new CommentService(commentRepository, todoRepository);
        //when
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            commentService.deleteComment(comment_id, loginedUser);
        });
        //then
        assertEquals("댓글 작성자만 삭제할 수 있습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("댓글 삭제 테스트 - 댓글 삭제 성공")
    void deleteCommentTest3() {
        //given
        Long comment_id = 1L;
        User commentWriter = new User("loginedUser", "password");
        TodoRequestDto todoRequestDto = new TodoRequestDto("할일 제목", "할일 내용");
        User todoWriter = new User("tester", "tester1234");
        Todo todo = new Todo(todoRequestDto, todoWriter);

        Comment comment = new Comment(new CommentRequestDto("댓글 원본"), commentWriter, todo);
        User loginedUser = new User("loginedUser", "password");
        given(commentRepository.findById(comment_id)).willReturn(Optional.of(comment));
        CommentService commentService = new CommentService(commentRepository, todoRepository);
        //when
        commentService.deleteComment(comment_id, loginedUser);

        //then
        verify(commentRepository).delete(any(Comment.class));
    }
}