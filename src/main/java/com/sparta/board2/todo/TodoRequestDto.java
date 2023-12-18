package com.sparta.board2.todo;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TodoRequestDto {
    @Size(min = 1, max = 500, message = "1자 이상 500자 이하의 제목을 입력해 주세요!")
    String title;
    @Size(min = 1, max = 5000, message = "1자 이상 5000자 이하의 내용을 입력해 주세요!")
    String contents;
}
