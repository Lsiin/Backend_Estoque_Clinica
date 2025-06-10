package com.project.backend.exceptions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private BeanPropertyBindingResult bindingResult;

    @Test
    public void testHandleDuplicateDataException() {
        GlobalExceptionHandler.DuplicateDataException ex = new GlobalExceptionHandler.DuplicateDataException("Duplicate data found");
        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleDuplicateDataException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Duplicate data found", response.getBody().get("message"));
    }

    @Test
    public void testHandleInvalidCpfFormatException() {
        GlobalExceptionHandler.InvalidCpfFormatException ex = new GlobalExceptionHandler.InvalidCpfFormatException("Invalid CPF format");
        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleInvalidCpfFormatException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid CPF format", response.getBody().get("message"));
    }

    @Test
    public void testHandleInvalidPhoneNumberFormatException() {
        GlobalExceptionHandler.InvalidPhoneNumberFormatException ex = new GlobalExceptionHandler.InvalidPhoneNumberFormatException("Invalid phone number");
        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleInvalidPhoneNumberFormatException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid phone number", response.getBody().get("message"));
    }

    @Test
    public void testHandleEnumIncorrectException() {
        GlobalExceptionHandler.EnumIncorretException ex = new GlobalExceptionHandler.EnumIncorretException("Invalid enum value");
        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleEnumIncorrectException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid enum value", response.getBody().get("message"));
    }

    @Test
    public void testHandleInvalidEmailException() {
        GlobalExceptionHandler.InvalidEmailFormatException ex = new GlobalExceptionHandler.InvalidEmailFormatException("Invalid email format");
        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleInvalidEmailException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid email format", response.getBody().get("message"));
    }

    @Test
    public void testHandleProductNotFoundException() {
        GlobalExceptionHandler.ProductNotFoundException ex = new GlobalExceptionHandler.ProductNotFoundException("Product not found");
        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleProductNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Product not found", response.getBody().get("message"));
    }

    @Test
    public void testHandleInvalidPriceException() {
        GlobalExceptionHandler.InvalidPriceException ex = new GlobalExceptionHandler.InvalidPriceException("Invalid price");
        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleInvalidPriceException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid price", response.getBody().get("message"));
    }

    @Test
    public void testHandleInvalidQuantityException() {
        GlobalExceptionHandler.InvalidQuantityException ex = new GlobalExceptionHandler.InvalidQuantityException("Invalid quantity");
        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleInvalidQuantityException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid quantity", response.getBody().get("message"));
    }

    @Test
    public void testHandleInvalidDateException() {
        GlobalExceptionHandler.InvalidDateException ex = new GlobalExceptionHandler.InvalidDateException("Invalid date");
        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleInvalidDateException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid date", response.getBody().get("message"));
    }

    @Test
    public void testHandleStockInsufficientException() {
        GlobalExceptionHandler.StockInsufficientException ex = new GlobalExceptionHandler.StockInsufficientException("Insufficient stock");
        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleStockInsufficientException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Insufficient stock", response.getBody().get("message"));
    }

    @Test
    public void testHandleStockOperationException() {
        GlobalExceptionHandler.StockOperationException ex = new GlobalExceptionHandler.StockOperationException("Stock operation failed");
        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleStockOperationException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Stock operation failed", response.getBody().get("message"));
    }

    @Test
    public void testHandleResourceNotFoundException() {
        GlobalExceptionHandler.ResourceNotFoundException ex = new GlobalExceptionHandler.ResourceNotFoundException("Resource not found");
        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleResourceNotFoundException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Resource not found", response.getBody().get("message"));
    }

    @Test
    public void testHandleResourceIsNullException() {
        GlobalExceptionHandler.ResourceBeNullException ex = new GlobalExceptionHandler.ResourceBeNullException("Resource cannot be null");
        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleResourceIsNullException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Resource cannot be null", response.getBody().get("message"));
    }

    @Test
    public void testHandleUserNotFoundException() {
        GlobalExceptionHandler.UserNotFoundException ex = new GlobalExceptionHandler.UserNotFoundException("User not found");
        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleUserNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody().get("message"));
    }

    @Test
    public void testHandleSupplierNotFoundException() {
        GlobalExceptionHandler.SupplierNotFoundException ex = new GlobalExceptionHandler.SupplierNotFoundException("Supplier not found");
        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleSupplierNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Supplier not found", response.getBody().get("message"));
    }

    @Test
    public void testHandleStockNotFoundException() {
        GlobalExceptionHandler.StockNotFoundException ex = new GlobalExceptionHandler.StockNotFoundException("Stock not found");
        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleStockNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Stock not found", response.getBody().get("message"));
    }

    @Test
    public void testHandleValidationExceptions() {
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);
        FieldError fieldError1 = new FieldError("objectName", "field1", "Field 1 is invalid");
        FieldError fieldError2 = new FieldError("objectName", "field2", "Field 2 is invalid");

        when(bindingResult.getFieldErrors()).thenReturn(java.util.Arrays.asList(fieldError1, fieldError2));

        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleValidationExceptions(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals("Field 1 is invalid", response.getBody().get("field1"));
        assertEquals("Field 2 is invalid", response.getBody().get("field2"));
    }
}