package com.payme.app.controller;

import com.payme.app.exception.ErrorResponse;
import com.payme.app.exception.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(
            UserNotFoundException exception, HttpServletRequest request){

        log.warn("User not found: {}", exception.getMessage());

        ErrorResponse userNotFoundErrorResponse = ErrorResponse.builder()
                .errorTimestamp(LocalDateTime.now())
                .message(exception.getMessage())
                .statusCode(HttpStatus.NOT_FOUND.value())
                .requestPath(request.getRequestURI())
                .build();

        return new ResponseEntity<>(userNotFoundErrorResponse, HttpStatus.NOT_FOUND);
    }
}
