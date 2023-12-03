package com.sparta.board2.repository;

import com.sparta.board2.dto.TodoRequestDto;
import com.sparta.board2.entity.Comment;
import com.sparta.board2.entity.Todo;
import com.sparta.board2.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CommentRepositoryTest extends RepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    @DisplayName("findByTodoOrderByCreatedAtDesc(Todo todo)")
    void test1() {
        //given
        User todouser = new User("user1", "password");
        User commentUser = new User("user2", "password");
        userRepository.save(todouser);
        userRepository.save(commentUser);
        Todo todo = new Todo(new TodoRequestDto("할일 제목", "할일 내용"), todouser);

        Comment comment1 = new Comment("댓글 내용1", commentUser);
        todo.addCommentList(comment1);
        Comment comment2 = new Comment("댓글 내용2", commentUser);
        todo.addCommentList(comment2);
        todoRepository.save(todo);
        //when
        List<Comment> comments = commentRepository.findByTodoOrderByCreatedAtDesc(todo);

        //then
        assertThat(comments.size()).isEqualTo(2);
        assertThat(comments.get(0).getContents()).isEqualTo("댓글 내용2");
    }


}