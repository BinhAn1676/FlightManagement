package com.binhan.flightmanagement.exception;

public class WrongDateLogicException extends RuntimeException{
    public WrongDateLogicException(String errorMessage){
        super(errorMessage);
    }
}
