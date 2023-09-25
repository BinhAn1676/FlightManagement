package com.binhan.flightmanagement.exception;

public class RoleNotFoundException extends RuntimeException{
    public RoleNotFoundException(String errorMessage){
        super(errorMessage);
    }
}
