package com.binhan.flightmanagement.repository;

import com.binhan.flightmanagement.models.FlightEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface FlightRepository extends JpaRepository<FlightEntity,Long> {
    List<FlightEntity> findByArrivalAirport_IdIn(List<Long> ids);
    List<FlightEntity> findByDepartureAirport_IdIn(List<Long> ids);
    void deleteByIdIn(List<Long> ids);
}
