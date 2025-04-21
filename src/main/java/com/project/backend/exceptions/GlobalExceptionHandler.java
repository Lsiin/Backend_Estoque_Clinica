package com.project.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


    @ExceptionHandler(DuplicateDataException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateDataException(DuplicateDataException ex) {
        return handleException(ex, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(InvalidCpfFormatException.class)
    public ResponseEntity<Map<String, String>> handleInvalidCpfFormatException(InvalidCpfFormatException ex) {
        return handleException(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidPhoneNumberFormatException.class)
    public ResponseEntity<Map<String, String>> handleInvalidPhoneNumberFormatException(InvalidPhoneNumberFormatException ex) {
        return handleException(ex, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(EnumIncorretException.class)
    public ResponseEntity<Map<String, String>> handleEnumIncorrectException(EnumIncorretException ex) {
        return handleException(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidEmailFormatException.class)
    public ResponseEntity<Map<String, String>> handleInvalidEmailException(InvalidEmailFormatException ex) {
        return handleException(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleProductNotFoundException(ProductNotFoundException ex) {
        return handleException(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidPriceException.class)
    public ResponseEntity<Map<String, String>> handleInvalidPriceException(InvalidPriceException ex) {
        return handleException(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidQuantityException.class)
    public ResponseEntity<Map<String, String>> handleInvalidQuantityException(InvalidQuantityException ex) {
        return handleException(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidDateException.class)
    public ResponseEntity<Map<String, String>> handleInvalidDateException(InvalidDateException ex) {
        return handleException(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(StockInsufficientException.class)
    public ResponseEntity<Map<String, String>> handleStockInsufficientException(StockInsufficientException ex) {
        return handleException(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(StockOperationException.class)
    public ResponseEntity<Map<String, String>> handleStockOperationException(StockOperationException ex) {
        return handleException(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return handleException(ex, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ResourceBeNullException.class)
    public ResponseEntity<Map<String, String>> handleResourceIsNullException(ResourceBeNullException ex) {
        return handleException(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFoundException(UserNotFoundException ex) {
        return handleException(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SupplierNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleSupplierNotFoundException(SupplierNotFoundException ex) {
        return handleException(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(StockNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleStockNotFoundException(StockNotFoundException ex) {
        return handleException(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }


    public static class InvalidEmailFormatException extends RuntimeException {
        public InvalidEmailFormatException(String message) {
            super(message);
        }
    }
public static class ResourceBeNullException extends RuntimeException {
        public ResourceBeNullException(String message) {
            super(message);
        }
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
    }

    public static class ProductNotFoundException extends RuntimeException {
        public ProductNotFoundException(String message) {
            super(message);
        }
    }

    public static class InvalidPriceException extends RuntimeException {
        public InvalidPriceException(String message) {
            super(message);
        }
    }

    public static class InvalidQuantityException extends RuntimeException {
        public InvalidQuantityException(String message) {
            super(message);
        }
    }

    public static class InvalidDateException extends RuntimeException {
        public InvalidDateException(String message) {
            super(message);
        }
    }

    public static class StockInsufficientException extends RuntimeException {
        public StockInsufficientException(String message) {
            super(message);
        }
    }

    public static class StockOperationException extends RuntimeException {
        public StockOperationException(String message) {
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

    public static class InvalidCnpjFormatException extends RuntimeException {
        public InvalidCnpjFormatException(String message) {
            super(message);
        }
    }

    public static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) {
            super(message);
        }
    }

    public static class InvalidPhoneNumberFormatException extends RuntimeException {
        public InvalidPhoneNumberFormatException(String message) {
            super(message);
        }
    }

    public static class EnumIncorretException extends RuntimeException {
        public EnumIncorretException(String message) {
            super(message);
        }
    }

    public static class SupplierNotFoundException extends RuntimeException {
        public SupplierNotFoundException(String message) {
            super(message);
        }
    }

    public static class StockNotFoundException extends RuntimeException {
        public StockNotFoundException(String message) {
            super(message);
        }
    }
}