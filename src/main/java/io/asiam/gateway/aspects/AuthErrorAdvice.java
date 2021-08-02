package io.asiam.gateway.aspects;

import io.asiam.gateway.exceptions.AuthError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class AuthErrorAdvice {
    @ExceptionHandler(value = {AuthError.class})
    public ResponseEntity<Map<String, Object>> authErrorHandler(AuthError e) {
        return new ResponseEntity<>(
                Map.of("success", false, "message", e.getMessage()),
                new HttpHeaders(),
                HttpStatus.FORBIDDEN
        );
    }
}
