package com.binhan.flightmanagement.exception;

public class AircraftNotFoundException extends RuntimeException{
    public AircraftNotFoundException(String errorMessage){
        super(errorMessage);
    }
}
