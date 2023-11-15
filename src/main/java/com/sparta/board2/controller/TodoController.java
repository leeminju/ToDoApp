package com.sparta.board2.controller;

import com.sparta.board2.dto.TodoRequestDto;
import com.sparta.board2.dto.TodoResponseDto;
import com.sparta.board2.security.UserDetailsImpl;
import com.sparta.board2.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TodoController {

    private final TodoService todoService;

    @PostMapping("/post")
    public ResponseEntity<TodoResponseDto> createTodo(@RequestBody TodoRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return todoService.createTodo(requestDto, userDetails.getUser());
    }

    @GetMapping("/posts")
    public Map<String, List<TodoResponseDto>> getTodoList() {
        return todoService.getTodoList();
    }

    @GetMapping("/post/{id}")
    public TodoResponseDto getTodoById(@PathVariable Long id) {
        return todoService.getTodoById(id);
    }

    @PutMapping("/post/{id}")
    public ResponseEntity<?> updateTodo(@PathVariable Long id, @RequestBody TodoRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return todoService.updateTodo(id, requestDto, userDetails.getUser());
    }

    @DeleteMapping("/post/{id}")
    public ResponseEntity<?> deleteTodo(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return todoService.deleteTodo(id, userDetails.getUser());
    }

    @PutMapping("/post/{id}/{finished}")
    public ResponseEntity<?> updatefinished(@PathVariable Long id, @PathVariable boolean finished,  @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return todoService.updatefinished(id, finished,userDetails.getUser());
    }
}
