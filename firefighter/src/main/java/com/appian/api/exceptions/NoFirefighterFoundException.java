package com.appian.api.exceptions;

public class NoFirefighterFoundException extends RuntimeException{
    public NoFirefighterFoundException() {
        super("No fire fighters available to fight the fire");
    }
}
