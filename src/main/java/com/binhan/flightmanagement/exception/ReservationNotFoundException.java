package com.binhan.flightmanagement.exception;

public class ReservationNotFoundException extends RuntimeException{
    public ReservationNotFoundException(String err){
        super(err);
    }
}
