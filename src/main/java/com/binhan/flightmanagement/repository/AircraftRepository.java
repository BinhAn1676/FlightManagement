package com.binhan.flightmanagement.repository;

import com.binhan.flightmanagement.models.AircraftEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AircraftRepository extends JpaRepository<AircraftEntity,Long> {
}
