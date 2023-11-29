package com.binhan.flightmanagement.service.impl;

import com.binhan.flightmanagement.dto.ReservationDto;
import com.binhan.flightmanagement.exception.ReservationNotFoundException;
import com.binhan.flightmanagement.models.PaymentEntity;
import com.binhan.flightmanagement.models.ReservationEntity;
import com.binhan.flightmanagement.repository.PaymentRepository;
import com.binhan.flightmanagement.repository.ReservationRepository;
import com.binhan.flightmanagement.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {

    private PaymentRepository paymentRepository;
    private ReservationRepository reservationRepository;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository,ReservationRepository reservationRepository){
        this.paymentRepository=paymentRepository;
        this.reservationRepository=reservationRepository;
    }

    @Override
    public void savePayment(String amount, String bankCode, Long reservationId) {
        if(!reservationRepository.existsById(reservationId)){
            throw new ReservationNotFoundException("cant find reservation");
        }
        ReservationEntity reservationEntity = reservationRepository.findById(reservationId).get();
        reservationEntity.setBookingStatus("paid");
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm dd/MM/yyyy");
        PaymentEntity paymentEntity;
        try {
            paymentEntity = PaymentEntity.builder()
                    .paymentMethod("VNPAY")
                    .paymentTime(dateFormat.parse(new Date().toString()))
                    .amount(Double.parseDouble(amount))
                    .reservation(reservationEntity)
                    .build();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        reservationRepository.save(reservationEntity);
        paymentRepository.save(paymentEntity);
    }

    @Override
    public List<PaymentEntity> findAllPayments() {
        List<PaymentEntity>paymentEntities=paymentRepository.findAll();
        return paymentEntities;
    }

    @Override
    public List<PaymentEntity> findPaymentsWithSorting(String field) {
        List<PaymentEntity> paymentEntities;
        if(field == null){
            paymentEntities = paymentRepository.findAll();
        }else{
            paymentEntities = paymentRepository.findAll(Sort.by(Sort.Direction.ASC,field));
        }
        return paymentEntities;
    }

    @Override
    public Page<PaymentEntity> findPaymentsWithPaginationAndSorting(int offset, int pageSize, String field) {
        Page<PaymentEntity> paymentEntities;
        if (field == null) {
            paymentEntities = paymentRepository.findAll(PageRequest.of(offset, pageSize));
        } else {
            paymentEntities = paymentRepository.findAll(PageRequest.of(offset, pageSize).withSort(Sort.by(field)));
        }
        return paymentEntities;
    }
}
