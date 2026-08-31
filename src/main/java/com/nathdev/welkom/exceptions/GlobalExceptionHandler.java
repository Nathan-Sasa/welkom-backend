package com.nathdev.welkom.exceptions;

import com.nathdev.welkom.exceptions.accessDenied.AccessDeniedCustomException;
import com.nathdev.welkom.exceptions.customizedTemplate.CustomizedAccessDeniedException;
import com.nathdev.welkom.exceptions.customizedTemplate.CustomizedTemplateAlreadyExistsException;
import com.nathdev.welkom.exceptions.customizedTemplate.CustomizedTemplateNotFoundException;
import com.nathdev.welkom.exceptions.event.EventNotFoundException;
import com.nathdev.welkom.exceptions.guest.GuestNotFoundException;
import com.nathdev.welkom.exceptions.invitation.InvitationAlreadyExistsException;
import com.nathdev.welkom.exceptions.invitation.InvitationNotFoundException;
import com.nathdev.welkom.exceptions.template.TemplateNotFoundException;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedCustomException.class)
    public ResponseEntity<@NotNull Map<String, String>> handleAccessDeniedCustomException(
            AccessDeniedCustomException exception
    ) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(Map.of("message", exception.getMessage()));
    }

    @ExceptionHandler(TemplateNotFoundException.class)
    public ResponseEntity<@NotNull Map<String, String>> handleTemplateNotFound(
            TemplateNotFoundException exception
    ) {
        return  ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", exception.getMessage()));
    }

    @ExceptionHandler(CustomizedTemplateAlreadyExistsException.class)
    public ResponseEntity<@NotNull Map<String, String>> handleCustomizedTemplateAlreadyExists(
            CustomizedTemplateAlreadyExistsException exception
    ) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("message", exception.getMessage()));
    }

    @ExceptionHandler(CustomizedAccessDeniedException.class)
    public ResponseEntity<@NotNull Map<String, String>> handleCustomizedAccessDeniedException(
            CustomizedAccessDeniedException exception
    ){
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(Map.of("message", exception.getMessage()));
    }

    @ExceptionHandler(CustomizedTemplateNotFoundException.class)
    public ResponseEntity<@NotNull Map<String, String>> handleCustomizedTemplateNotFound(
            CustomizedTemplateNotFoundException exception
    ){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", exception.getMessage()));
    }

    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<@NotNull Map<String, String>> handleEventNotFound(
            EventNotFoundException exception
    ) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", exception.getMessage()));
    }

    @ExceptionHandler(GuestNotFoundException.class)
    public ResponseEntity<@NotNull Map<String, String>> handleGuestNotFound(
            GuestNotFoundException exception
    ) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", exception.getMessage()));
    }


    @ExceptionHandler(InvitationAlreadyExistsException.class)
    public ResponseEntity<@NotNull Map<String, String>> handleInvitationAlreadyExists(
            InvitationAlreadyExistsException exception
    ){
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("message", exception.getMessage()));
    }

    @ExceptionHandler(InvitationNotFoundException.class)
    public ResponseEntity<@NotNull Map<String, String>> handleInvitationNotFound(
            InvitationNotFoundException exception
    ){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", exception.getMessage()));
    }
}
