package com.nathdev.welkom.exceptions;

import com.nathdev.welkom.exceptions.accessDenied.AccessDeniedCustomException;
import com.nathdev.welkom.exceptions.badRequest.BadRequestCustomException;
import com.nathdev.welkom.exceptions.customizedTemplate.CustomizedAccessDeniedException;
import com.nathdev.welkom.exceptions.customizedTemplate.CustomizedTemplateAlreadyExistsException;
import com.nathdev.welkom.exceptions.customizedTemplate.CustomizedTemplateNotFoundException;
import com.nathdev.welkom.exceptions.event.EventIllegalCustomException;
import com.nathdev.welkom.exceptions.event.EventNotFoundException;
import com.nathdev.welkom.exceptions.exist.AlreadyExistCustomException;
import com.nathdev.welkom.exceptions.guest.GuestNotFoundException;
import com.nathdev.welkom.exceptions.invitation.InvitationAlreadyExistsException;
import com.nathdev.welkom.exceptions.invitation.InvitationNotFoundException;
import com.nathdev.welkom.exceptions.notFound.NotFoundCustomException;
import com.nathdev.welkom.exceptions.template.TemplateNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@Slf4j
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

    @ExceptionHandler(EventIllegalCustomException.class)
    public ResponseEntity<@NotNull Map<String, String>> handleEventIllegalCustomException(
            EventIllegalCustomException exception
    ){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
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

    @ExceptionHandler(NotFoundCustomException.class)
    public ResponseEntity<@NotNull Map<String, String>> handleNotFoundException(
            NotFoundCustomException exception
    ){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", exception.getMessage()));
    }

    @ExceptionHandler(AlreadyExistCustomException.class)
    public ResponseEntity<@NotNull Map<String, String>> handleAlreadyExistCustomException(
            AlreadyExistCustomException exception
    ){
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("message", exception.getMessage()));
    }

    @ExceptionHandler(BadRequestCustomException.class)
    public ResponseEntity<@NotNull Map<String, String>> handleBadRequestCustomException(
            BadRequestCustomException exception
    ){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<@NotNull ErrorResponse> handeAll(
            Exception exception,
            HttpServletRequest request
    ) {
        log.error("Unexpected error", exception);

        ErrorResponse error = new ErrorResponse(
        ) {
            @Override
            public @NotNull HttpStatusCode getStatusCode() {
                return HttpStatus.INTERNAL_SERVER_ERROR;
            }

            @Override
            public @NotNull ProblemDetail getBody() {
//                return ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Une erreur est survenue, veillez réessayez plus tard !");
            }
        };

        return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
