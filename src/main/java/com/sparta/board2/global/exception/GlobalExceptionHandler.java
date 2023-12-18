package com.sparta.board2.global.exception;

import com.sparta.board2.global.response.CustomResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<CustomResponseEntity> methodArgumentNotValidHandler(MethodArgumentNotValidException ex) {
        StringBuilder message = new StringBuilder();
        for (ObjectError result : ex.getBindingResult().getAllErrors()) {
            message.append(result.getDefaultMessage());
            message.append("\n");
        }

        return new ResponseEntity<>(
                // HTTP body
                new CustomResponseEntity(
                        message.toString()
                        , HttpStatus.BAD_REQUEST.value()),

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