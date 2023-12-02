package com.sparta.board2.service;

import com.sparta.board2.dto.TodoRequestDto;
import com.sparta.board2.dto.TodoResponseDto;

import com.sparta.board2.entity.Todo;
import com.sparta.board2.entity.User;
import com.sparta.board2.repository.TodoRepository;
import com.sparta.board2.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class TodoServiceTest {
    @Mock
    TodoRepository todoRepository;
    @Mock
    UserRepository userRepository;

    TodoRequestDto createRequestDto;
    TodoRequestDto updateRequestDto;
    User todoWriter;
    User logineduser;
    Long todoId;


    @BeforeEach
    void setUp() {
        createRequestDto = new TodoRequestDto("할일 제목", "할일 내용");
        updateRequestDto = new TodoRequestDto("할일 제목 수정", "할일 내용 수정");
        todoWriter = new User("writer", "password");
        logineduser = new User("user", "password");
        todoId = 1L;
    }

    @Test
    @DisplayName("할일 생성")
    void createTodoTest() {
        //given
        Todo todo = new Todo(createRequestDto, logineduser);
        given(todoRepository.save(any(Todo.class))).willReturn(todo);

        TodoService todoService = new TodoService(todoRepository, userRepository);
        //when
        TodoResponseDto todoResponseDto = todoService.createTodo(createRequestDto, logineduser);
        //then
        assertEquals(createRequestDto.getContents(), todoResponseDto.getContents());
        assertEquals(createRequestDto.getTitle(), todoResponseDto.getTitle());
        assertEquals(logineduser.getUsername(), todoResponseDto.getUsername());
    }

    @Test
    @DisplayName("user 별 할일 목록 조회")
    void getTodoListTest() {
        //given
        List<User> users = new ArrayList<>();
        User user1 = new User("minju", "password");
        users.add(user1);
        User user2 = new User("jinyoung", "password");
        users.add(user2);

        user1.getTodoList().addAll(List.of(new Todo[]{new Todo(new TodoRequestDto("minju 할일1", "내용1"), user1),
                new Todo(new TodoRequestDto("minju 할일2", "내용2"), user1),
                new Todo(new TodoRequestDto("minju 할일3", "내용3"), user1)
        }));
        user2.getTodoList().addAll(List.of(new Todo[]{new Todo(new TodoRequestDto("jinyoung 할일1", "내용1"), user2),
                new Todo(new TodoRequestDto("jinyoung 할일2", "내용2"), user2),
                new Todo(new TodoRequestDto("jinyoung 할일3", "내용3"), user2),
                new Todo(new TodoRequestDto("jinyoung 할일4", "내용4"), user2)
        }));

        given(userRepository.findAll()).willReturn(users);
        given(todoRepository.findByUserOrderByCreatedAtDesc(user1)).willReturn(user1.getTodoList());
        given(todoRepository.findByUserOrderByCreatedAtDesc(user2)).willReturn(user2.getTodoList());

        TodoService todoService = new TodoService(todoRepository, userRepository);
        //when
        Map<String, List<TodoResponseDto>> reponseMap = todoService.getTodoList();
        //then
        assertEquals(2, reponseMap.size());
        assertEquals(3, reponseMap.get(user1.getUsername()).size());
        assertEquals(4, reponseMap.get(user2.getUsername()).size());

    }

    @Test
    @DisplayName("선택한 할일 조회 - 할일 존재하지 않음")
    void getTodoByIdTest1() {
        //given
        TodoService todoService = new TodoService(todoRepository, userRepository);
        //when
        Exception exception = assertThrows(NullPointerException.class, () -> {
            todoService.getTodoById(todoId);
        });
        //then
        assertEquals("할일이 존재하지 않습니다", exception.getMessage());
    }

    @Test
    @DisplayName("선택한 할일 조회 - 성공")
    void getTodoByIdTest2() {
        //given
        Todo todo = new Todo(createRequestDto, todoWriter);
        given(todoRepository.findById(todoId)).willReturn(Optional.of(todo));

        TodoService todoService = new TodoService(todoRepository, userRepository);
        //when
        TodoResponseDto todoResponseDto = todoService.getTodoById(todoId);

        //then
        assertEquals(createRequestDto.getTitle(), todoResponseDto.getTitle());
        assertEquals(createRequestDto.getContents(), todoResponseDto.getContents());
        assertEquals(todoWriter.getUsername(), todoResponseDto.getUsername());
    }

    @Test
    @DisplayName("할일 수정 - 할일이 존재하지 않음")
    void updateTodoTest() {
        //given
        TodoService todoService = new TodoService(todoRepository, userRepository);
        //when
        Exception exception = assertThrows(NullPointerException.class, () -> {
            todoService.updateTodo(todoId, updateRequestDto, logineduser);
        });
        //then
        assertEquals("할일이 존재하지 않습니다", exception.getMessage());
    }

    @Test
    @DisplayName("할일 수정 - 작성자가 아님")
    void updateTodoTest2() {
        //given
        Todo todo = new Todo(createRequestDto, todoWriter);
        given(todoRepository.findById(todoId)).willReturn(Optional.of(todo));

        TodoService todoService = new TodoService(todoRepository, userRepository);
        //when
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            todoService.updateTodo(todoId, updateRequestDto, logineduser);
        });
        //then
        assertEquals("작성자만 수정할 수 있습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("할일 수정 - 성공")
    void updateTodoTest3() {
        //given
        todoWriter = new User("user", "password");
        Todo todo = new Todo(createRequestDto, todoWriter);
        given(todoRepository.findById(todoId)).willReturn(Optional.of(todo));

        TodoService todoService = new TodoService(todoRepository, userRepository);
        //when
        TodoResponseDto todoResponseDto = todoService.updateTodo(todoId, updateRequestDto, logineduser);
        //then
        assertEquals(updateRequestDto.getTitle(), todoResponseDto.getTitle());
        assertEquals(updateRequestDto.getContents(), todoResponseDto.getContents());
        assertEquals(logineduser.getUsername(), todoResponseDto.getUsername());
    }

    @Test
    @DisplayName("할일 삭제 - 할일이 존재하지 않음")
    void deleteTodoTest() {
        //given
        TodoService todoService = new TodoService(todoRepository, userRepository);
        //when
        Exception exception = assertThrows(NullPointerException.class, () -> {
            todoService.deleteTodo(todoId, logineduser);
        });
        //then
        assertEquals("할일이 존재하지 않습니다", exception.getMessage());
    }

    @Test
    @DisplayName("할일 삭제 - 작성자가 아님")
    void deleteTodoTest2() {
        //given
        Todo todo = new Todo(createRequestDto, todoWriter);
        given(todoRepository.findById(todoId)).willReturn(Optional.of(todo));

        TodoService todoService = new TodoService(todoRepository, userRepository);
        //when
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            todoService.deleteTodo(todoId, logineduser);
        });
        //then
        assertEquals("작성자만 삭제할 수 있습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("할일 삭제 - 성공")
    void deleteTodoTest3() {
        //given
        todoWriter = new User("user", "password");
        Todo todo = new Todo(createRequestDto, todoWriter);
        given(todoRepository.findById(todoId)).willReturn(Optional.of(todo));
        TodoService todoService = new TodoService(todoRepository, userRepository);
        //when
        todoService.deleteTodo(todoId, logineduser);
        //then
        verify(todoRepository).delete(any(Todo.class));
    }


    @Test
    @DisplayName("할일 완료 - 할일이 존재하지 않음")
    void updatefinishedTest() {
        //given
        TodoService todoService = new TodoService(todoRepository, userRepository);
        //when
        Exception exception = assertThrows(NullPointerException.class, () -> {
            todoService.updateFinished(todoId, true, logineduser);
        });
        //then
        assertEquals("할일이 존재하지 않습니다", exception.getMessage());
    }

    @Test
    @DisplayName("할일 완료 - 작성자가 아님")
    void updatefinishedTest2() {
        //given
        Todo todo = new Todo(createRequestDto, todoWriter);
        given(todoRepository.findById(todoId)).willReturn(Optional.of(todo));

        TodoService todoService = new TodoService(todoRepository, userRepository);
        //when
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            todoService.updateFinished(todoId, true, logineduser);
        });
        //then
        assertEquals("작성자만 완료/취소할 수 있습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("할일 완료 - 성공")
    void updatefinishedTest3() {
        //given
        todoWriter = new User("user", "password");
        Todo todo = new Todo(createRequestDto, todoWriter);
        given(todoRepository.findById(todoId)).willReturn(Optional.of(todo));

        TodoService todoService = new TodoService(todoRepository, userRepository);
        //when
        boolean result = todoService.updateFinished(todoId, true, logineduser);
        //then
        assertTrue(result);
    }
}