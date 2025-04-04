package com.payme.authentication.controller;

import com.payme.authentication.exception.ErrorResponse;
import com.payme.authentication.exception.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException
            (UserNotFoundException exception, HttpServletRequest request){

        log.warn("User not found: {}", exception.getMessage());

        ErrorResponse userNotFoundErrorResponse = ErrorResponse.builder()
                .errorTimestamp(LocalDateTime.now())
                .message(exception.getMessage())
                .statusCode(HttpStatus.NOT_FOUND.value())
                .requestPath(request.getRequestURI())
                .build();

        return new ResponseEntity<>(userNotFoundErrorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException
            (MethodArgumentNotValidException exception, HttpServletRequest request){

        log.warn("User entered invalid fields: {}", exception.getMessage());

        ErrorResponse invalidMethodArgumentResponse = ErrorResponse.builder()
                .errorTimestamp(LocalDateTime.now())
                .message(exception.getMessage())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .requestPath(request.getRequestURI())
                .build();

        return new ResponseEntity<>(invalidMethodArgumentResponse, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ErrorResponse> generateErrorResponse(
            RuntimeException runtimeException,
            HttpServletRequest request,
            HttpStatus status){

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorTimestamp(LocalDateTime.now())
                .message(runtimeException.getMessage())
                .statusCode(status.value())
                .requestPath(request.getRequestURI())
                .build();

        return new ResponseEntity<>(errorResponse, status);
    }

}
