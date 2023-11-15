package com.example.forums_backend.exception;

public class AppException extends Exception{
    public AppException(String errorMessage) {
        super(errorMessage);
    }
}
