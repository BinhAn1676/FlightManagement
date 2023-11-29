package com.binhan.flightmanagement.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "payments")
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "reservation_id")
    @JsonIgnoreProperties("payments")
    private ReservationEntity reservation;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "payment_time")
    private Date paymentTime;

    private Double amount;

    private String paymentMethod;

}
