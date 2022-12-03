package com.prac.join.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    DUPLICATE_USER_NAME(HttpStatus.CONFLICT,""),
    USERNAME_NOT_FOUND(HttpStatus.NOT_FOUND,""),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED,"")
    ;

    private HttpStatus httpStatus;
    private String message;
}
