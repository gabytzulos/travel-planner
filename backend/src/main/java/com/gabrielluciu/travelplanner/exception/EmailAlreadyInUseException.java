package com.gabrielluciu.travelplanner.exception;

public class EmailAlreadyInUseException extends RuntimeException {
    public EmailAlreadyInUseException(String email) {
        super(String.format("Email '%s' is already in use.", email));
    }
}
