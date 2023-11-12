package com.binhan.flightmanagement.exception;

public class CountryExistedException extends RuntimeException{
    public CountryExistedException(String errorMessage){
        super(errorMessage);
    }
}
