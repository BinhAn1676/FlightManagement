package com.binhan.flightmanagement.repository;

import com.binhan.flightmanagement.models.AircraftEntity;
import com.binhan.flightmanagement.models.AirportEntity;
import com.binhan.flightmanagement.models.FlightEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Repository
public interface FlightRepository extends JpaRepository<FlightEntity,Long> {
    List<FlightEntity> findByArrivalAirport_IdIn(List<Long> ids);
    List<FlightEntity> findByDepartureAirport_IdIn(List<Long> ids);
    List<FlightEntity> findByArrivalAirport_Id(Long id);
    List<FlightEntity> findByDepartureAirport_Id(Long id);

    List<FlightEntity> findByAircraft_Id(Long id);
    void deleteByIdIn(List<Long> ids);
    Boolean existsByAircraftAndDepartureAirportAndArrivalAirportAndStatusAndSeatsAndArrivalTimeAndDepartureTime(AircraftEntity aircraftEntity,
                                                                                                                AirportEntity departureAirport,
                                                                                                                AirportEntity arrivalAirport,
                                                                                                                String status, Integer seats, Date ArrivalTime, Date departureTime);
}
