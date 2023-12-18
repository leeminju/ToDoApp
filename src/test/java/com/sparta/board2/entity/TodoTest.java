package com.sparta.board2.entity;

import com.sparta.board2.todo.TodoRequestDto;
import com.sparta.board2.todo.TodoResponseDto;
import com.sparta.board2.todo.Todo;
import com.sparta.board2.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class TodoTest {

    @Test
    @DisplayName("todo.update()")
    void test1() {
        //given
        User user = new User("testUser", "passwort");
        Todo todo = new Todo(new TodoRequestDto("할일 제목", "할일 내용"), user);
        String title = "제목";
        String contents = "내용";
        TodoRequestDto todoRequestDto = new TodoRequestDto(title, contents);

        //when
        todo.update(todoRequestDto);

        //then
        assertEquals(title, todo.getTitle());
        assertEquals(contents, todo.getContents());
    }

    @Test
    @DisplayName("TodoResponseDto(Todo todo)")
    void test2() {
        //given
        User user = new User("testUser", "passwort");
        Todo todo = new Todo(new TodoRequestDto("할일 제목", "할일 내용"), user);

        //when
        TodoResponseDto todoResponseDto = new TodoResponseDto(todo);

        //then
        assertEquals(todo.getTitle(), todoResponseDto.getTitle());
        assertEquals(todo.getContents(), todoResponseDto.getContents());
        assertFalse(todoResponseDto.isFinished());
        assertEquals(todo.getUser().getUsername(), todoResponseDto.getUsername());
    }
}