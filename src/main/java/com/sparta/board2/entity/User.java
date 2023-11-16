package com.sparta.board2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")

public class User {
    @Id
    private String username;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user")
    private List<Todo> todoList = new ArrayList<>();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.todoList = new ArrayList<>();
    }

    public void addTodoList(Todo todo) {
        this.todoList.add(todo);
        todo.setUser(this); // 외래 키(연관 관계) 설정
    }
}
