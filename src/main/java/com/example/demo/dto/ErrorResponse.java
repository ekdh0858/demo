package com.example.demo.dto;

public record ErrorResponse(ErrorDetail error) {
    public static ErrorResponse of(String code, String message) {
        return new ErrorResponse(new ErrorDetail(code, message));
    }

    public record ErrorDetail(String code, String message) {}
}
