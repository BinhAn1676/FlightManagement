package com.binhan.flightmanagement.repository;

import com.binhan.flightmanagement.models.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity,Long> {
}
