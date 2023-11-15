package com.sparta.board2.repository;

import com.sparta.board2.entity.Todo;
import com.sparta.board2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo,Long> {

    List<Todo> findAllByUserOrderByFinished(User user);

    List<Todo> findAllByUserUsernameOrderByFinished(String username);

}
