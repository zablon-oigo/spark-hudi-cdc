package gmart.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import gmart.domain.dto.ErrorDto;
import gmart.exception.InventoryNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Validation Error
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleValidationExceptions(
            MethodArgumentNotValidException ex
    ) {
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(FieldError::getDefaultMessage)
                .orElse("Validation failed");

        return new ResponseEntity<>(
                new ErrorDto(errorMessage),
                HttpStatus.BAD_REQUEST
        );
    }

    // Not found error
    @ExceptionHandler(InventoryNotFoundException.class)
    public ResponseEntity<ErrorDto> handleInventoryNotFound(
            InventoryNotFoundException ex
    ) {
        return new ResponseEntity<>(
                new ErrorDto(ex.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }
}