package com.binhan.flightmanagement.service;

public interface EmailSenderService {
    void sendEmail(String to, String subject, String message);

    boolean checkNumber(Integer numberToCheck);
}
