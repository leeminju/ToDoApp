package com.sparta.board2.comment;

import com.sparta.board2.global.entity.Timestamped;
import com.sparta.board2.todo.Todo;
import com.sparta.board2.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "comments")
@NoArgsConstructor
public class Comment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_id", nullable = false)
    private Todo todo;

    @ManyToOne
    @JoinColumn(name = "username", nullable = false)
    private User user;

    public Comment(CommentRequestDto requestDto, User user, Todo todo) {
        this.contents = requestDto.getContents();
        this.todo = todo;
        this.user = user;
    }

    public Comment(String contents, User user) {
        this.contents = contents;
        this.user = user;
    }

    public void update(CommentRequestDto requestDto) {
        this.contents = requestDto.getContents();
    }


    public void addTodo(Todo todo) {
        this.todo = todo;
    }
}
