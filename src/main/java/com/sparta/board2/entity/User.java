package com.sparta.board2.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User implements Persistable<String> {
    @Id
    private String username;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user")
    private List<Todo> todoList = new ArrayList<>();

    @Transient
    private boolean isNew = true;

    @PrePersist
    @PostLoad
    void markNotNew() {
        this.isNew = false;
    }


    @Builder
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String getId() {
        return this.username;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }
}
