package com.sparta.board2.global.exception;

import com.sparta.board2.global.response.CustomResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<CustomResponseEntity> methodArgumentNotValidHandler(MethodArgumentNotValidException ex) {
        return new ResponseEntity<>(
                // HTTP body
                new CustomResponseEntity(ex.getBindingResult().getAllErrors().get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST.value()),

                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler({RestApiException.class})
    public ResponseEntity<CustomResponseEntity> customExceptionHandler(RestApiException ex) {
        RestApiException restApiException = new RestApiException(ex.getErrorMessage(), ex.getStatusCode());
        return new ResponseEntity<>(
                // HTTP body
                new CustomResponseEntity(restApiException.getErrorMessage(), restApiException.getStatusCode().value()),
                // HTTP status code
                ex.getStatusCode()
        );
    }
}