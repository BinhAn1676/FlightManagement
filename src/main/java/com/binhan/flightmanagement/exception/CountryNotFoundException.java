package com.binhan.flightmanagement.exception;

public class CountryNotFoundException extends RuntimeException{
    public CountryNotFoundException(String errorMessage){
        super(errorMessage);
    }
}
