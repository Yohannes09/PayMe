package com.payme.authentication.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
public class ErrorResponse {
    private LocalDateTime errorTimestamp;
    private String message;
    private int statusCode;
    private String requestPath;
}
