package com.appian.api.exceptions;

public class InvalidFirefighterException extends Exception {
    public InvalidFirefighterException(int numFirefighters) {
        super("Invalid number of fire fighters: " + numFirefighters + ". The minimum value is 1.");
    }
}
