package com.springBoot.Oauth2Example.exception;

public class InactiveStatusException extends RuntimeException {
    public InactiveStatusException(String message) {
        super(message);
    }
}
