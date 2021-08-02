package io.asiam.gateway.exceptions;

public class AuthError extends RuntimeException {
    public AuthError(String message) {
        super("Auth error: " + message);
    }
}
