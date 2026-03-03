package com.gabrielluciu.travelplanner.exception;

public class NoAuthenticatedUserException extends RuntimeException {
    public NoAuthenticatedUserException() {
        super("No authenticated user found.");
    }
}
