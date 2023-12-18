package com.sparta.board2.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 400
    IS_DUPLICATE_USERNAME("중복된 회원 입니다.", HttpStatus.BAD_REQUEST),
    NOT_EQUALS_PASSWORD("비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),

    //NOT_FOUND
    NOT_FOUND_USER("등록된 회원이 없습니다.", HttpStatus.NOT_FOUND),
    NOT_FOUND_TODO("할일이 존재하지 않습니다.", HttpStatus.FOUND),
    NOT_FOUND_COMMENT("해당 댓글 존재하지 않습니다.", HttpStatus.FOUND),

    //FORBIDDEN
    CAN_NOT_MODIFY_TODO("작성자만 수정할 수 있습니다.", HttpStatus.FORBIDDEN),
    CAN_NOT_DELETE_TODO("작성자만 삭제할 수 있습니다.", HttpStatus.FORBIDDEN),
    CAN_NOT_MODIFY_COMMENT("댓글 작성자만 수정할 수 있습니다.", HttpStatus.FORBIDDEN),
    CAN_NOT_DELETE_COMMENT("댓글 작성자만 삭제할 수 있습니다.", HttpStatus.FORBIDDEN),
    CAN_NOT_FINISH_TODO("작성자만 완료/취소할 수 있습니다.", HttpStatus.FORBIDDEN),

    ;

    private String errorMessage;
    private HttpStatus statusCode;
}