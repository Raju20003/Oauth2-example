package com.springBoot.Oauth2Example.exception;

public class ResourceIncorrectException extends RuntimeException{
    public ResourceIncorrectException(String message) {
        super(message);
    }
}
