package com.sparta.board2.entity;

import com.sparta.board2.dto.TodoRequestDto;
import com.sparta.board2.dto.TodoResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TodoTest {

    @Test
    @DisplayName("todo.update()")
    void test1() {
        //given
        User user = new User("testUser", "passwort");
        Todo todo = new Todo(1L, "제목1", "내용1", true, user, new ArrayList<>());
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
        Todo todo = new Todo(1L, "제목", "내용", true, user, new ArrayList<>());

        //when
        TodoResponseDto todoResponseDto = new TodoResponseDto(todo);

        //then
        assertEquals(todo.getId(), todoResponseDto.getId());
        assertEquals(todo.getTitle(), todoResponseDto.getTitle());
        assertEquals(todo.getContents(), todoResponseDto.getContents());
        assertEquals(todo.isFinished(), todoResponseDto.isFinished());
        assertEquals(todo.getUser().getUsername(), todoResponseDto.getUsername());
    }
}