package com.sparta.board2.dto;

import com.sparta.board2.entity.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDto {
    @NotBlank
    private String contents;
}
