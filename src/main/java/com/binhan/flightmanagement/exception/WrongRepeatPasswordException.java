package com.binhan.flightmanagement.exception;

public class WrongRepeatPasswordException extends RuntimeException{
    public WrongRepeatPasswordException(String errorMessage){
        super(errorMessage);
    }
}
