package com.sparta.board2.repository;

import com.sparta.board2.entity.Comment;
import com.sparta.board2.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    List<Comment> findByTodoOrderByCreatedAtDesc(Todo todo);
}
