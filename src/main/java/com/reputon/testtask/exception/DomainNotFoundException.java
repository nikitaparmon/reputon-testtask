package com.reputon.testtask.exception;

public class DomainNotFoundException extends RuntimeException {

    public DomainNotFoundException(String domain) {
        super("Domain '" + domain + "' not found");
    }
}
