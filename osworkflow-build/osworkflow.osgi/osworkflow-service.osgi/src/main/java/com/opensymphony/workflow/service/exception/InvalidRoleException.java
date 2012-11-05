package com.opensymphony.workflow.service.exception;

public class InvalidRoleException extends WorkflowException {

    private static final long serialVersionUID = 2979634921912512706L;

    public InvalidRoleException() {
    }

    public InvalidRoleException(Throwable cause) {
        super(cause);
    }

    public InvalidRoleException(String message) {
        super(message);
    }

    public InvalidRoleException(String message, Throwable cause) {
        super(message, cause);
    }
}