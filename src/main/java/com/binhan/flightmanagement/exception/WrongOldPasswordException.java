package com.binhan.flightmanagement.exception;

public class WrongOldPasswordException extends RuntimeException{
    public WrongOldPasswordException(String errorMessage){
        super(errorMessage);
    }
}
