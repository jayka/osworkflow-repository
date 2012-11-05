package com.opensymphony.workflow.service.exception;

public class InvalidEntryStateException extends WorkflowException {

    private static final long serialVersionUID = 7486959808018814649L;

    public InvalidEntryStateException() {
    }

    public InvalidEntryStateException(Throwable cause) {
        super(cause);
    }

    public InvalidEntryStateException(String message) {
        super(message);
    }

    public InvalidEntryStateException(String message, Throwable cause) {
        super(message, cause);
    }
}