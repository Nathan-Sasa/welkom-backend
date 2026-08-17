package com.nathdev.welkom.exceptions;

import com.nathdev.welkom.exceptions.template.TemplateNotFoundException;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(TemplateNotFoundException.class)
    public ResponseEntity<@NotNull Map<String, String>> handleTemplateNotFound(
            TemplateNotFoundException exception
    ) {
        return  ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", exception.getMessage()));
    }
}
