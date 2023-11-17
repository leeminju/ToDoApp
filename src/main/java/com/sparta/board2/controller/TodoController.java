package com.sparta.board2.controller;

import com.sparta.board2.dto.TodoRequestDto;
import com.sparta.board2.dto.TodoResponseDto;
import com.sparta.board2.security.UserDetailsImpl;
import com.sparta.board2.service.TodoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j(topic = "TODO 검증")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TodoController {

    private final TodoService todoService;

    @PostMapping("/post")
    public ResponseEntity<?> createTodo(@RequestBody @Valid TodoRequestDto requestDto,
                                        BindingResult bindingResult,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails ) {
        // Validation 예외처리
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if (fieldErrors.size() > 0) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("제목과 내용을 모두 입력해 주세요!");
        }

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
    public ResponseEntity<?> updateTodo(@PathVariable Long id,
                                        @RequestBody TodoRequestDto requestDto,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return todoService.updateTodo(id, requestDto, userDetails.getUser());
    }

    @DeleteMapping("/post/{id}")
    public ResponseEntity<?> deleteTodo(@PathVariable Long id,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return todoService.deleteTodo(id, userDetails.getUser());
    }

    @PutMapping("/post/{id}/{finished}")
    public ResponseEntity<?> updatefinished(@PathVariable Long id,
                                            @PathVariable boolean finished,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return todoService.updatefinished(id, finished, userDetails.getUser());
    }
}
