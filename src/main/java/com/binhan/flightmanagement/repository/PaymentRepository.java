package com.binhan.flightmanagement.repository;

import com.binhan.flightmanagement.models.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity,Long> {
}
