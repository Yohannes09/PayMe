package com.payme.authentication.exception;

import com.payme.internal.exception.BaseGlobalExceptionHandler;
import com.payme.internal.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class AuthGlobalExceptionHandler extends BaseGlobalExceptionHandler {
    // Errors that should not be returned for the client for various reasons like
    // security and UX.
    @ExceptionHandler({RoleNotFoundException.class, IllegalStateException.class})
    public ResponseEntity<ErrorResponse> handleInternalExceptions(
            Exception exception, HttpServletRequest request
    ){
        log.error("FATAL ERROR {}\nTrace:{}", exception.getMessage(), exception.getStackTrace());
        return generateErrorResponse(
                "Oops, something went wrong. Please try again later.",
                HttpStatus.INTERNAL_SERVER_ERROR,
                request
        );
    }


    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(
            AuthenticationException exception, HttpServletRequest servletRequest
    ){
        log.error("Error during authentication: {}", exception.getMessage());
        return generateErrorResponse(
                exception.getMessage(),
                HttpStatus.UNAUTHORIZED,
                servletRequest
        );
    }


    @ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleNotFoundExceptions(
             RuntimeException exception, HttpServletRequest request
    ){
        log.warn("User not found: {}", exception.getMessage());
        return generateErrorResponse(
                "User not found.",
                HttpStatus.NOT_FOUND,
                request
        );
    }


    @ExceptionHandler(CredentialUpdateException.class)
    public ResponseEntity<ErrorResponse> handleCredentialException(
            CredentialUpdateException exception, HttpServletRequest request
    ){
        log.info("User credential update failed: {}", exception.getMessage());
        return generateErrorResponse(
                "User not found.",
                HttpStatus.BAD_REQUEST,
                request
        );

    }

}
