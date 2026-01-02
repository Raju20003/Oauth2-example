package com.springBoot.Oauth2Example.exception;

public class ConfirmTokenExpiredException extends RuntimeException {
    public ConfirmTokenExpiredException(String message) {
        super(message);
    }
}
