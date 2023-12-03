package com.sparta.board2.repository;

import com.sparta.board2.dto.TodoRequestDto;
import com.sparta.board2.entity.Todo;
import com.sparta.board2.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TodoRepositoryTest extends RepositoryTest {
    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    @DisplayName("findByUserOrderByCreatedAtDesc(User user)")
    void test1() {
        //given
        User user = new User("user", "password");
        userRepository.save(user);

        Todo todo1 = new Todo(new TodoRequestDto("할일 제목1", "할일 내용1"));
        Todo todo2 = new Todo(new TodoRequestDto("할일 제목2", "할일 내용2"));

        todo1.addUser(user);
        todo2.addUser(user);

        todoRepository.save(todo1);
        todoRepository.save(todo2);

        //when
        List<Todo> todoList = todoRepository.findByUserOrderByCreatedAtDesc(user);

        //then
        assertThat(todoList.size()).isEqualTo(2);
        assertThat(todoList.get(0).getTitle()).isEqualTo("할일 제목2");
    }

    @Test
    @Transactional
    @DisplayName("아무것도 없이 할일 저장")
    void test2() {
        //given
        Todo todo1 = new Todo();

        //when-then
        assertThrows(DataIntegrityViolationException.class,
                () -> todoRepository.save(todo1));
    }
}