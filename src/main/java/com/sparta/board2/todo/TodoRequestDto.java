package com.sparta.board2.todo;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TodoRequestDto {
    @NotBlank(message = "할일 제목은 공백일 수 없습니다.")
    String title;
    @NotBlank(message = "할일 내용은 공백일 수 없습니다.")
    String contents;
}
