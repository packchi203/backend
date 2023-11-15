package com.example.forums_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class AccountException extends Exception{

    public AccountException(String errorMessage) {
        super(errorMessage);
    }
}
