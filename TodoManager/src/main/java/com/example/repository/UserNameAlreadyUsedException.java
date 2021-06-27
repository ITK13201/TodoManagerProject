package com.example.repository;

public class UserNameAlreadyUsedException extends Exception {
    public UserNameAlreadyUsedException(String msg) {
        super(msg);
    }
}
