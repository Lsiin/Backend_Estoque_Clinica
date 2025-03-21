package com.project.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGlobalException(Exception ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "An unexpected error occurred");
        response.put("ERROR", ex.getClass().getSimpleName());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    public static class DuplicateDataException extends RuntimeException {
        public DuplicateDataException(String message) {
            super(message);
        }
    }
    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
        public static class InvalidCpfFormatException extends RuntimeException {
            public InvalidCpfFormatException(String message) {
                super(message);
            }
        }
        public static class InvalidCepFormatException extends RuntimeException {
            public InvalidCepFormatException(String message) {
                super(message);
            }
        }
        public static class InvalidPhoneNumberFormatException extends RuntimeException {
            public InvalidPhoneNumberFormatException(String message) {
                super(message);
            }
        }
    }
}
