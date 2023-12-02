package com.sparta.board2.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto {
    private String username;
    private String password;
}
