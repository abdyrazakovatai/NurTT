package dev.nurtt.exception.response;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@Builder
public class ExceptionResponse {
    private HttpStatus status;
    private LocalDateTime timestamp;
    private String exceptionsClassName;
    private String message;
}