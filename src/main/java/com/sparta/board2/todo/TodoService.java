package com.sparta.board2.todo;

import com.sparta.board2.global.exception.ErrorCode;
import com.sparta.board2.global.exception.RestApiException;
import com.sparta.board2.user.User;
import com.sparta.board2.user.UserRepository;
import lombok.RequiredArgsConstructor;
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
                () -> new RestApiException(ErrorCode.NOT_FOUND_TODO)
        );
        return new TodoResponseDto(todo);
    }

    @Transactional
    public TodoResponseDto updateTodo(Long id, TodoRequestDto requestDto, User user) {
        Todo todo = todoRepository.findById(id).orElseThrow(
                () -> new RestApiException(ErrorCode.NOT_FOUND_TODO)
        );

        if (!todo.getUser().getUsername().equals(user.getUsername())) {
            throw new RestApiException(ErrorCode.CAN_NOT_MODIFY_TODO);
        }

        todo.update(requestDto);
        return new TodoResponseDto(todo);
    }

    public void deleteTodo(Long id, User user) {
        Todo todo = todoRepository.findById(id).orElseThrow(
                () -> new RestApiException(ErrorCode.NOT_FOUND_TODO)
        );

        if (!todo.getUser().getUsername().equals(user.getUsername())) {
            throw new RestApiException(ErrorCode.CAN_NOT_DELETE_TODO);
        }

        todoRepository.delete(todo);
    }

    @Transactional
    public boolean updateFinished(Long id, boolean finished, User user) {
        Todo todo = todoRepository.findById(id).orElseThrow(
                () -> new RestApiException(ErrorCode.NOT_FOUND_TODO)
        );
        if (!todo.getUser().getUsername().equals(user.getUsername())) {
            throw new RestApiException(ErrorCode.CAN_NOT_FINISH_TODO);
        }

        todo.changefinished(finished);
        return todo.isFinished();
    }


}
