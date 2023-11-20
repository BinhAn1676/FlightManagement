package com.binhan.flightmanagement.dto;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ReservationDto {
    private Long id;

    private String username;

    private Long flightId;

    private Date reservationTime;

    private Integer seatNumber;

    private String bookingStatus;
}
