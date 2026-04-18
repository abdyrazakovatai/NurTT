package dev.nurtt.exception.handler;

import dev.nurtt.exception.BadRequestException;
import dev.nurtt.exception.NotFoundException;
import dev.nurtt.exception.response.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalException {

    private ExceptionResponse buildResponse(HttpStatus status, String className, String message) {
        return ExceptionResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status)
                .exceptionsClassName(className)
                .message(message)
                .build();
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse handleNotFound(NotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getClass().getName(), ex.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleBadRequest(BadRequestException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getClass().getName(), ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return buildResponse(
                HttpStatus.BAD_REQUEST,
                ex.getClass().getSimpleName(),
                "Неверный тип параметра запроса: " + ex.getMessage()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public ExceptionResponse handleUnexpected(Exception ex) {
//        ex.printStackTrace();
//        return buildResponse(
//                HttpStatus.INTERNAL_SERVER_ERROR,
//                ex.getClass().getName(),
//                "Внутренняя ошибка сервера. Мы уже разбираемся."
//        );
//    }

//    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
//    public ResponseEntity<?> handleAuthenticationError(AuthenticationCredentialsNotFoundException ex) {
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
//                Map.of(
//                        "timestamp", LocalDateTime.now().toString(),
//                        "status", 401,
//                        "error", "Unauthorized",
//                        "message", ex.getMessage()
//                )
//        );
//    }

    @ExceptionHandler(org.springframework.web.server.ResponseStatusException.class)
    public ResponseEntity<ExceptionResponse> handleResponseStatusException(org.springframework.web.server.ResponseStatusException ex) {
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
        return ResponseEntity.status(status)
                .body(buildResponse(
                        status,
                        ex.getClass().getName(),
                        ex.getReason() != null ? ex.getReason() : ex.getMessage()
                ));
    }
}