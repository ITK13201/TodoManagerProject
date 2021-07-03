package com.example.repository.exception;

public class UserNameAlreadyUsedException extends Exception {
    public UserNameAlreadyUsedException(String msg) {
        super(msg);
    }
}
