package com.sparta.board2.controller;

import com.sparta.board2.dto.TodoRequestDto;
import com.sparta.board2.dto.TodoResponseDto;
import com.sparta.board2.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TodoController {

    private final TodoService todoService;

    @PostMapping("/post")
    public TodoResponseDto createTodo(@RequestBody TodoRequestDto requestDto){
        return todoService.createTodo(requestDto);
    }

}
