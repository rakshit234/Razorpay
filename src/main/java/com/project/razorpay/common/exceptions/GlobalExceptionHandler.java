package com.project.razorpay.common.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateResources(
            DuplicateResourceException ex){

        log.warn("Email address already exists {}", ex.getMessage());

        ErrorResponse error = ErrorResponse.of(ex.getErrorCode(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);

    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
            ResourceNotFoundException ex){

        String errorCode = ex.getResourceName().toUpperCase()+"_NOT_FOUND";

        ErrorResponse error = ErrorResponse.of(errorCode, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);

    }
}
