package com.sparta.board2.dto;

import com.sparta.board2.entity.Todo;

import java.time.LocalDateTime;

public class TodoResponseDto {
    private Long id;
    private String title;
    private String username;
    private String contents;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public TodoResponseDto(Todo todo) {
        this.id=todo.getId();
        this.title=todo.getTitle();
        this.contents=todo.getContents();
        this.createdAt=todo.getCreatedAt();
        this.modifiedAt=todo.getModifiedAt();
    }
}
