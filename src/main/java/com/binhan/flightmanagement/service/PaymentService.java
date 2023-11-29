package com.binhan.flightmanagement.service;

import com.binhan.flightmanagement.models.PaymentEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PaymentService {
    void savePayment(String amount, String bankCode, Long reservationId);

    List<PaymentEntity> findAllPayments();


    List<PaymentEntity> findPaymentsWithSorting(String field);

    Page<PaymentEntity> findPaymentsWithPaginationAndSorting(int offset, int pageSize, String field);
}
