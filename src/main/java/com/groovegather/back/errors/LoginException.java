package com.groovegather.back.errors;

public class LoginException extends RuntimeException {
    public LoginException(String message) {
        super(message);
    }
}