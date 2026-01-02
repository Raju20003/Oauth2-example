package com.springBoot.Oauth2Example.exception;

public class TokenUnauthorizedException extends RuntimeException {
    public TokenUnauthorizedException(String message) {
        super(message);
    }
}