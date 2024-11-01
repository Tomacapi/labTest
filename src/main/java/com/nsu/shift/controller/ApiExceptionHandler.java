package com.nsu.shift.controller;

import com.nsu.shift.exception.ServiceException;
import com.nsu.shift.requests.ServiceErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ServiceErrorResponse> handle(ServiceException exception) {
        return new ResponseEntity<>(new ServiceErrorResponse(exception.getErrorCode().ordinal(), exception.getLocalizedMessage()),
                exception.getErrorCode().getHttpStatus());
    }


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handle(RuntimeException exception) {
        return new ResponseEntity<>(exception.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
