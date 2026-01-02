package com.springBoot.Oauth2Example.exception;

public class LoggedOutException extends RuntimeException {
    public LoggedOutException(String message) {
        super(message);
    }
}
