package com.sparta.board2.service;

import com.sparta.board2.dto.TodoRequestDto;
import com.sparta.board2.dto.TodoResponseDto;
import com.sparta.board2.entity.Todo;
import com.sparta.board2.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return todoRepository.findByOrderByFinished().stream().map(TodoResponseDto::new).toList();
    }

    public TodoResponseDto getTodoById(Long id) {
        Todo todo = todoRepository.findById(id).orElseThrow(
                () -> new NullPointerException("존재하지 않습니다")
        );
        return new TodoResponseDto(todo);
    }

    @Transactional
    public TodoResponseDto updateTodo(Long id, TodoRequestDto requestDto) {
        Todo todo = todoRepository.findById(id).orElseThrow(
                () -> new NullPointerException("존재하지 않습니다")
        );
        todo.update(requestDto);

        return new TodoResponseDto(todo);
    }

    @Transactional
    public ResponseEntity<?> updatefinished(Long id, boolean finished) {
        Todo todo = todoRepository.findById(id).orElseThrow(
                () -> new NullPointerException("존재하지 않습니다")
        );

        todo.setFinished(finished);
        return ResponseEntity.status(HttpStatus.OK).body("완료 여부" + finished + "로 변경");
    }
}
