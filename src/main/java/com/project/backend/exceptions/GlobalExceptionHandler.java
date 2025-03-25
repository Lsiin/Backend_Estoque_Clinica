package com.project.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<Map<String, String>> handleException(RuntimeException ex, HttpStatus status) {
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(InvalidEmailFormatException.class)
    public ResponseEntity<Map<String, String>> handleInvalidEmailException(InvalidEmailFormatException ex) {
        return handleException(ex, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(DuplicateDataException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateDataException(DuplicateDataException ex) {
        return handleException(ex, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFoundException(UserNotFoundException ex) {
        return handleException(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidCpfFormatException.class)
    public ResponseEntity<Map<String, String>> handleInvalidCpfFormatException(InvalidCpfFormatException ex) {
        return handleException(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidCepFormatException.class)
    public ResponseEntity<Map<String, String>> handleInvalidCepFormatException(InvalidCepFormatException ex) {
        return handleException(ex, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(InvalidCnpjFormatException.class)
    public ResponseEntity<Map<String, String>> handleInvalidCnpjFormatException(InvalidCnpjFormatException ex) {
        return handleException(ex, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<Map<String, String>> HandleCategoryNotFoundException(InvalidPhoneNumberFormatException ex) {
        return handleException(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidPhoneNumberFormatException.class)
    public ResponseEntity<Map<String, String>> handleInvalidPhoneNumberFormatException(InvalidPhoneNumberFormatException ex) {
        return handleException(ex, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(SupplierNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleSuplierNotFoundException(SupplierNotFoundException ex) {
        return handleException(ex, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(StockNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleStockNotFoundException(StockNotFoundException ex) {
        return handleException(ex, HttpStatus.NOT_FOUND);
    }

    public static class InvalidEmailFormatException extends RuntimeException {
        public InvalidEmailFormatException(String message) {
            super(message);
        }
    }

    public static class DuplicateDataException extends RuntimeException {
        public DuplicateDataException(String message) {
            super(message);
        }
    }

    public static class CategoryNotFoundException extends RuntimeException {
        public CategoryNotFoundException(String message) {
            super(message);
        }
    }

    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }

    }

    public static class SupplierNotFoundException extends RuntimeException {
        public SupplierNotFoundException(String message) {
            super(message);
        }
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

    public static class InvalidCnpjFormatException extends RuntimeException {
        public InvalidCnpjFormatException(String message) {
            super(message);
        }
    }

    public static class StockNotFoundException extends RuntimeException {
        public StockNotFoundException(String message) {
            super(message);
        }
    }

}