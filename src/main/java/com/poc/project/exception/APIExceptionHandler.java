package com.poc.project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class APIExceptionHandler{

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<?> studentNotFoundExceptionHandler(StudentNotFoundException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(StudentAlreadyExistsException.class)
    public ResponseEntity<?> studentAlreadyExistsExceptionHandler(StudentAlreadyExistsException exception){
        return new ResponseEntity<>(exception.getMessage(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> invalidConstraintHandler(ConstraintViolationException exception){
        return new ResponseEntity<>(exception.getConstraintViolations().stream().map(s->s.getMessage()),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<?> customValidationErrorHandling(WebExchangeBindException exception){
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach(error->{
            String field = ((FieldError) error).getField();
            String defaultMessage = error.getDefaultMessage();
            errors.put(field,defaultMessage);
        });
        return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
    }
}
