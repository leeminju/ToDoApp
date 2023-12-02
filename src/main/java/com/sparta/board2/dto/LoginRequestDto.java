package com.sparta.board2.dto;

import com.sparta.board2.annotation.Password;
import com.sparta.board2.annotation.Username;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto {
    @Username
    private String username;
    @Password
    private String password;
}
