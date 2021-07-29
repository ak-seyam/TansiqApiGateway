package com.example.routingandfilteringgateway.exceptions;

public class AuthError extends RuntimeException {
    public AuthError(String message) {
        super("Auth error: " + message);
    }
}
