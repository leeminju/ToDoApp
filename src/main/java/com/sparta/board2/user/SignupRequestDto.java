package com.sparta.board2.user;

import com.sparta.board2.global.annotation.Password;
import com.sparta.board2.global.annotation.Username;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequestDto {
    @Username
    private String username;

    @Password
    private String password;
}