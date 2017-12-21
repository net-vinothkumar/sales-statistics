package com.mglvm.salesstatistics.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

//@ControllerAdvice TODO
public class SalesStatisticsControllerAdvice {

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<ErrorInfo> handleClientInputException(Exception e) {
        ErrorInfo errorInfo = new ErrorInfo(e.getMessage());
        errorInfo.setErrorCode(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }
}
