package com.sparta.board2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequestDto {
    @NotBlank(message = "username은 공백일 수 없습니다.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)[a-z\\d]+$", message = "username은 영어 소문자, 숫자 조합만 가능합니다.")
    @Size(min = 4, max = 10, message = "username은 4자 이상 10자 이하로 입력해주세요")
    private String username;

    @NotBlank(message = "password는 공백일 수 없습니다.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]+$", message = "password는 영어 대문자, 소문자, 숫자 조합만 가능합니다.")
    @Size(min = 8, max = 15, message = "password는 8자 이상 15자 이하로 입력해주세요")
    private String password;
}