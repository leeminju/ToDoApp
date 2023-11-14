package com.sparta.board2.service;

import com.sparta.board2.dto.TodoRequestDto;
import com.sparta.board2.dto.TodoResponseDto;
import com.sparta.board2.entity.Todo;
import com.sparta.board2.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoResponseDto createTodo(TodoRequestDto requestDto) {
        Todo todo = todoRepository.save(new Todo(requestDto));
        return new TodoResponseDto(todo);
    }

    public List<TodoResponseDto> getMyTodoList() {
        return todoRepository.findAll().stream().map(TodoResponseDto::new).toList();
    }

    public TodoResponseDto getTodoById(Long id) {
        Todo todo = todoRepository.findById(id).orElseThrow(
                () -> new NullPointerException("존재하지 않습니다")
        );
        return new TodoResponseDto(todo);
    }
}
