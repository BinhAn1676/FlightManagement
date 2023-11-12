package com.binhan.flightmanagement.exception;

public class FlightExistedException extends RuntimeException{
    public FlightExistedException(String errorMessage){
        super(errorMessage);
    }
}
