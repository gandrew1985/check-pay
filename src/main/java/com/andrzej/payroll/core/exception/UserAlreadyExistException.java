package com.andrzej.payroll.core.exception;

public class UserAlreadyExistException extends Exception{
    public UserAlreadyExistException(final String message) {
        super(message);
    }
}
