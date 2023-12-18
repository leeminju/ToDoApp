package com.sparta.board2.todo;

import com.sparta.board2.comment.Comment;
import com.sparta.board2.global.entity.Timestamped;
import com.sparta.board2.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "todolist")
@NoArgsConstructor
@AllArgsConstructor
public class Todo extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    private boolean finished = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username", nullable = false)
    private User user;

    //게시글 삭제되면 댓글도 같이 삭제
    @OneToMany(mappedBy = "todo", cascade = CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<>();

    public Todo(TodoRequestDto todoRequestDto) {
        super();
        this.title = todoRequestDto.getTitle();
        this.contents = todoRequestDto.getContents();
    }

    public void addCommentList(Comment comment) {
        commentList.add(comment);
        comment.addTodo(this);// 외래 키(연관 관계) 설정
    }

    public Todo(TodoRequestDto requestDto, User user) {
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
        this.user = user;
        this.finished = false;
    }

    public void update(TodoRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
    }

    public void addUser(User user) {
        this.user = user;
    }

    public void changefinished(boolean finished) {
        this.finished = finished;
    }
}
