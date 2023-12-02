package com.sparta.board2.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@Table(name = "users")

public class User {
    @Id
    private String username;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Todo> todoList = new ArrayList<>();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.todoList = new ArrayList<>();
    }

}
