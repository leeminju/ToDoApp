package com.sparta.board2.comment;

import com.sparta.board2.comment.Comment;
import com.sparta.board2.todo.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    List<Comment> findByTodoOrderByCreatedAtDesc(Todo todo);
}
