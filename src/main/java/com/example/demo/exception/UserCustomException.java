package com.example.demo.exception;

import lombok.Getter;

@Getter
public class UserCustomException extends RuntimeException {
    private final String code;
    public UserCustomException(String code, String message) {
        super(message);
        this.code = code;
    }
}
