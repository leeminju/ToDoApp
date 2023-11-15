package com.sparta.board2.controller;

import com.sparta.board2.dto.TodoRequestDto;
import com.sparta.board2.dto.TodoResponseDto;
import com.sparta.board2.security.UserDetailsImpl;
import com.sparta.board2.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TodoController {

    private final TodoService todoService;

    @PostMapping("/post")
    public TodoResponseDto createTodo(@RequestBody TodoRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return todoService.createTodo(requestDto, userDetails.getUser());
    }

    @GetMapping("/posts")
    public List<TodoResponseDto> getMyTodoList(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return todoService.getMyTodoList(userDetails.getUser());
    }

    @GetMapping("/posts/{username}")
    public List<TodoResponseDto> getTodoList(@PathVariable String username) {
        return todoService.getTodoList(username);
    }

    @GetMapping("/post/{id}")
    public TodoResponseDto getTodoById(@PathVariable Long id) {
        return todoService.getTodoById(id);
    }

    @PutMapping("/post/{id}")
    public TodoResponseDto updateTodo(@PathVariable Long id, @RequestBody TodoRequestDto requestDto) {
        return todoService.updateTodo(id, requestDto);
    }

    @PutMapping("/post/{id}/{finished}")
    public ResponseEntity<?> updatefinished(@PathVariable Long id, @PathVariable boolean finished) {
        return todoService.updatefinished(id, finished);
    }
}
