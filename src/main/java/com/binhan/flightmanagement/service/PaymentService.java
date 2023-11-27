package com.binhan.flightmanagement.service;

public interface PaymentService {
    void savePayment(String amount, String bankCode, Long reservationId);
}
