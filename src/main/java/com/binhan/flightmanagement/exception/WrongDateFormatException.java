package com.binhan.flightmanagement.exception;

public class WrongDateFormatException extends RuntimeException{
    public WrongDateFormatException(String errorMessage){
        super(errorMessage);
    }
}
