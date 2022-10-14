package com.reputon.testtask.exception;

public class HtmlParseException extends RuntimeException {

    public HtmlParseException() {
        super("Failed to parse html");
    }
}
