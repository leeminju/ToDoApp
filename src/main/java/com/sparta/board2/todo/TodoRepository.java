package com.sparta.board2.todo;

import com.sparta.board2.todo.Todo;
import com.sparta.board2.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo,Long> {
    List<Todo> findByUserOrderByCreatedAtDesc(User user);
}
