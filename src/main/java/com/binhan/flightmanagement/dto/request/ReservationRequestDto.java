package com.binhan.flightmanagement.dto.request;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ReservationRequestDto {
    private Long id;

    private String username;

    private Long flightId;

    private Date reservationTime;

    private List<Integer> seatNumber;

    private String bookingStatus;
}
