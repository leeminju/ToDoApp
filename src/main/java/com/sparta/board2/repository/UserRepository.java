package com.sparta.board2.repository;

import com.sparta.board2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    List<User> findAllByOrderByUsername();
}
