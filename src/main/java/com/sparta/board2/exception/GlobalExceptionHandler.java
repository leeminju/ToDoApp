package com.sparta.board2.exception;

import com.sparta.board2.response.CustomResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<RestApiException> illegalArgumentExceptionHandler(IllegalArgumentException ex) {
        RestApiException restApiException = new RestApiException(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(
                // HTTP body
                restApiException,
                // HTTP status code
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler({NullPointerException.class})
    public ResponseEntity<RestApiException> nullPointerExceptionHandler(NullPointerException ex) {
        RestApiException restApiException = new RestApiException(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(
                // HTTP body
                restApiException,
                // HTTP status code
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<RestApiException> ArgumentNotValidHandler(MethodArgumentNotValidException ex) {
        RestApiException restApiException = new RestApiException(ex.getBindingResult().getAllErrors().get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(
                // HTTP body
                restApiException,
                // HTTP status code
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler({IllegalStateException.class})
    public ResponseEntity<RestApiException> AccessDeniedHandler(IllegalStateException ex) {
        RestApiException restApiException = new RestApiException(ex.getMessage(), HttpStatus.FORBIDDEN.value());
        return new ResponseEntity<>(
                // HTTP body
                restApiException,
                // HTTP status code
                HttpStatus.FORBIDDEN
        );
    }
}