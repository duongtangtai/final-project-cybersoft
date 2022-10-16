package com.example.jiraproject.common.exception;

public class JiraAuthenticationException extends RuntimeException {
    public JiraAuthenticationException(String message) {
        super(message);
    }
}
