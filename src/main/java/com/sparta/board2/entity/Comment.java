package com.sparta.board2.entity;

import com.sparta.board2.dto.CommentRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
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
        this.todo=todo;
        this.user=user;
    }
}
