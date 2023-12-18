package com.sparta.board2.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public class RestApiException extends RuntimeException{
    private String errorMessage;
    private HttpStatusCode statusCode;

    public RestApiException(ErrorCode errorCode) {
        errorMessage = errorCode.getErrorMessage();
        statusCode = errorCode.getStatusCode();
    }

}