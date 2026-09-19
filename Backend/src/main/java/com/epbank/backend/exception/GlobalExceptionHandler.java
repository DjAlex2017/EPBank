package com.epbank.backend.exception;

import com.epbank.backend.dto.ErrorResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

// @RestControllerAdvice - This class can handle exceptions
// thrown by any of my REST controllers.

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> handleResponseStatusException(
        ResponseStatusException exception
    ){
        ErrorResponse error = new ErrorResponse();

        error.setStatus(exception.getStatusCode().value());
        error.setMessage(exception.getReason());

        return ResponseEntity.status(exception.getStatusCode()).body(error);
    }
}
