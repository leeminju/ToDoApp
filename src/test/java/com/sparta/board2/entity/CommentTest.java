package com.sparta.board2.entity;

import com.sparta.board2.dto.CommentRequestDto;
import com.sparta.board2.dto.CommentResponseDto;
import com.sparta.board2.dto.TodoRequestDto;
import com.sparta.board2.dto.TodoResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;

class CommentTest {
    @Test
    @DisplayName("new Comment(CommentRequestDto requestDto, User user, Todo todo)")
    void test1() {
        //given

        CommentRequestDto commentRequestDto = new CommentRequestDto("댓글 내용");
        User commentUser = new User("testuser1", "password");
        User todoUser = new User("testuser2", "password");
        Todo todo = new Todo(new TodoRequestDto("할일 제목", "할일 내용"), todoUser);

        //when
        Comment comment = new Comment(commentRequestDto, commentUser, todo);
        //then
        assertEquals(commentRequestDto.getContents(), comment.getContents());
        assertEquals(commentUser, comment.getUser());
        assertEquals(todo, comment.getTodo());
    }

    @Test
    @DisplayName("update(CommentRequestDto requestDto)")
    void test2() {
        //given
        CommentRequestDto commentRequestDto = new CommentRequestDto("댓글 내용 수정");
        Comment comment = new Comment();
        //when
        comment.update(commentRequestDto);
        //then
        assertEquals(commentRequestDto.getContents(), comment.getContents());
    }

    @Test
    @DisplayName("CommentResponseDto(Comment comment)")
    void test3() {
        //given
        User commentUser = new User("testuser1", "password");
        User todoUser = new User("testuser2", "password");
        Todo todo = new Todo(new TodoRequestDto("할일 제목", "할일 내용"), todoUser);
        Comment comment = new Comment("댓글 내용", commentUser);
        //when
        CommentResponseDto commentResponseDto = new CommentResponseDto(comment);
        //then
        assertEquals(comment.getContents(), commentResponseDto.getContents());
        assertEquals(comment.getUser().getUsername(), commentResponseDto.getUsername());

    }
}