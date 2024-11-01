package com.nsu.shift.exception;

import org.springframework.http.HttpStatus;
import lombok.Getter;

@Getter
public enum ErrorCode {
    OBJECT_NOT_FOUND(HttpStatus.NOT_FOUND),
    INVALID_AMOUNT(HttpStatus.BAD_REQUEST),
    CONTACT_INFO_IS_TAKEN(HttpStatus.BAD_REQUEST),
    INVALID_TIME_PERIOD(HttpStatus.BAD_REQUEST);

    private final HttpStatus httpStatus;

    ErrorCode(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
