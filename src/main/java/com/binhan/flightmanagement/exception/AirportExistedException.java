package com.binhan.flightmanagement.exception;

public class AirportExistedException extends RuntimeException{
    public AirportExistedException(String errorMessage){
        super(errorMessage);
    }
}
