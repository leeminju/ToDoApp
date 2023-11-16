package com.sparta.board2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {
    @NotBlank
    @Pattern(regexp = "^[a-z0-9]+$", message = "영어 소문자와 숫자조합만 가능합니다.")
    @Size(min = 4, max = 10)
    private String username;
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "영어와 숫자조합만 가능합니다.")
    @Size(min = 8, max = 15)
    private String password;
}