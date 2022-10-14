package com.reputon.testtask.controller;

import com.reputon.testtask.exception.DomainNotFoundException;

import com.reputon.testtask.exception.HtmlParseException;
import com.reputon.testtask.exception.RemoteHostException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(DomainNotFoundException.class)
    ResponseEntity<String> domainNotFound(DomainNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({RemoteHostException.class, HtmlParseException.class})
    ResponseEntity<String> remoteServiceError(RuntimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_GATEWAY);
    }
}
