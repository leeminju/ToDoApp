package com.sparta.board2.controller;

import com.sparta.board2.dto.TodoRequestDto;
import com.sparta.board2.dto.TodoResponseDto;
import com.sparta.board2.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TodoController {

    private final TodoService todoService;

    @PostMapping("/post")
    public TodoResponseDto createTodo(@RequestBody TodoRequestDto requestDto) {
        return todoService.createTodo(requestDto);
    }

    @GetMapping("/posts")
    public List<TodoResponseDto> getMyTodoList() {
        return todoService.getMyTodoList();
    }

    @GetMapping("/post/{id}")
    public TodoResponseDto getTodoById(@PathVariable Long id) {
        return todoService.getTodoById(id);
    }

    @PutMapping("/post/{id}")
    public TodoResponseDto updateTodo(@PathVariable Long id, @RequestBody TodoRequestDto requestDto) {
        return todoService.updateTodo(id,requestDto);
    }

    @PutMapping("/post/{id}/{finished}")
    public ResponseEntity<?> updatefinished(@PathVariable Long id, @PathVariable boolean finished) {
        return todoService.updatefinished(id,finished);
    }
}
