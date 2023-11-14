package com.sparta.board2.service;

import com.sparta.board2.dto.TodoRequestDto;
import com.sparta.board2.dto.TodoResponseDto;
import com.sparta.board2.entity.Todo;
import com.sparta.board2.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoResponseDto createTodo(TodoRequestDto requestDto) {
        Todo todo=todoRepository.save(new Todo(requestDto));
        return new TodoResponseDto(todo);
    }
}
