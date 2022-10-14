package com.reputon.testtask.exception;

public class RemoteHostException extends RuntimeException {

    public RemoteHostException(String host) {
        super("Request to remote host '" + host + "' failed");
    }
}
