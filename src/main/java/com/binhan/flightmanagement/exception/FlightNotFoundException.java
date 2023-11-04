package com.binhan.flightmanagement.exception;

public class FlightNotFoundException extends RuntimeException {
    public FlightNotFoundException(String errorMessage){
        super(errorMessage);
    }
}
