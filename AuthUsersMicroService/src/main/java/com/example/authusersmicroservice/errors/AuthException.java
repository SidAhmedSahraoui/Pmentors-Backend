package com.example.authusersmicroservice.errors;

public class AuthException extends RuntimeException{
    public AuthException(String message, Throwable cause) {
        super(message, cause);
    }
}
