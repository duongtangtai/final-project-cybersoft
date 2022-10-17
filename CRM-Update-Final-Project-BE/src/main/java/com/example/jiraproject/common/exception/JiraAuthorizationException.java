package com.example.jiraproject.common.exception;

public class JiraAuthorizationException extends RuntimeException{
    public JiraAuthorizationException(String message) {
        super(message);
    }
}
