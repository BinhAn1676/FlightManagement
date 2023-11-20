package com.binhan.flightmanagement.exception;

public class AirportNotFoundException extends RuntimeException{
    public AirportNotFoundException(String errMsg){
        super(errMsg);
    }
}
