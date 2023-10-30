package com.binhan.flightmanagement.repository;

import com.binhan.flightmanagement.models.AirportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AirportRepository extends JpaRepository<AirportEntity,Long> {
}
