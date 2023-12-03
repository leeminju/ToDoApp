package com.sparta.board2.service;

import com.sparta.board2.dto.TodoRequestDto;
import com.sparta.board2.dto.TodoResponseDto;
import com.sparta.board2.entity.Todo;
import com.sparta.board2.entity.User;
import com.sparta.board2.repository.TodoRepository;
import com.sparta.board2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    public TodoResponseDto createTodo(TodoRequestDto requestDto, User user) {
        Todo todo = todoRepository.save(new Todo(requestDto, user));
        todo.addUser(user);
        return new TodoResponseDto(todo);
    }

    public Map<String, List<TodoResponseDto>> getTodoList() {
        List<User> users = userRepository.findAll();

        Map<String, List<TodoResponseDto>> map = new TreeMap<>();//TreeMap -  key값 오름차순 정렬(이름순)을 위해서

        for (User user : users) {
            List<Todo> todoList = todoRepository.findByUserOrderByCreatedAtDesc(user);//작성일 기준 내림차순으로 정렬
            List<TodoResponseDto> responseDtoList = new ArrayList<>();

            for (Todo todo : todoList) {
                responseDtoList.add(new TodoResponseDto(todo));
            }
            map.put(user.getUsername(), responseDtoList);
        }

        return map;
    }

    public TodoResponseDto getTodoById(Long id) {
        Todo todo = todoRepository.findById(id).orElseThrow(
                () -> new NullPointerException("할일이 존재하지 않습니다")
        );
        return new TodoResponseDto(todo);
    }

    @Transactional
    public TodoResponseDto updateTodo(Long id, TodoRequestDto requestDto, User user) {
        Todo todo = todoRepository.findById(id).orElseThrow(
                () -> new NullPointerException("할일이 존재하지 않습니다")
        );

        if (!todo.getUser().getUsername().equals(user.getUsername())) {
            throw new IllegalStateException("작성자만 수정할 수 있습니다.");
        }

        todo.update(requestDto);
        return new TodoResponseDto(todo);
    }

    public void deleteTodo(Long id, User user) {
        Todo todo = todoRepository.findById(id).orElseThrow(
                () -> new NullPointerException("할일이 존재하지 않습니다")
        );

        if (!todo.getUser().getUsername().equals(user.getUsername())) {
            throw new IllegalStateException("작성자만 삭제할 수 있습니다.");
        }

        todoRepository.delete(todo);
    }

    @Transactional
    public boolean updateFinished(Long id, boolean finished, User user) {
        Todo todo = todoRepository.findById(id).orElseThrow(
                () -> new NullPointerException("할일이 존재하지 않습니다")
        );
        if (!todo.getUser().getUsername().equals(user.getUsername())) {
            throw new IllegalStateException("작성자만 완료/취소할 수 있습니다.");
        }

        todo.changefinished(finished);
        return todo.isFinished();
    }


}
