package com.sparta.board2.todo;

import com.sparta.board2.global.response.CustomResponseEntity;
import com.sparta.board2.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j(topic = "TODO 검증")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TodoController {

    private final TodoService todoService;

    @PostMapping("/posts")
    public ResponseEntity<CustomResponseEntity> createTodo(@RequestBody @Valid TodoRequestDto requestDto,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        todoService.createTodo(requestDto, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new CustomResponseEntity(
                        "할일 추가 성공",
                        HttpStatus.CREATED.value()
                )
        );
    }


    @GetMapping("/posts")
    public Map<String, List<TodoResponseDto>> getTodoList() {
        return todoService.getTodoList();
    }

    @GetMapping("/posts/{id}")
    public TodoResponseDto getTodoById(@PathVariable Long id) {
        return todoService.getTodoById(id);
    }

    @PutMapping("/posts/{id}")
    public ResponseEntity<CustomResponseEntity> updateTodo(@PathVariable Long id,
                                                           @RequestBody @Valid TodoRequestDto requestDto,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        todoService.updateTodo(id, requestDto, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomResponseEntity(
                        "할일 수정 완료",
                        HttpStatus.OK.value()
                )
        );
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<CustomResponseEntity> deleteTodo(@PathVariable Long id,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        todoService.deleteTodo(id, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomResponseEntity(
                        "할일 삭제 완료",
                        HttpStatus.OK.value()
                )
        );
    }

    @PutMapping("/posts/{id}/finished/{finished}")
    public ResponseEntity<CustomResponseEntity> updateFinished(@PathVariable Long id,
                                                               @PathVariable boolean finished,
                                                               @AuthenticationPrincipal UserDetailsImpl userDetails) {
        boolean updatedFinished = todoService.updateFinished(id, finished, userDetails.getUser());
        if (updatedFinished) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new CustomResponseEntity(
                            "할일이 완료 처리 되었습니다",
                            HttpStatus.OK.value()
                    )
            );
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new CustomResponseEntity(
                            "할일이 완료 취소 되었습니다.",
                            HttpStatus.OK.value()
                    )
            );
        }
    }
}
