package com.binhan.flightmanagement.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "airports")
public class AirportEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String airportName;
    private String location;

    @ManyToOne
    @JoinColumn(name = "country_id")
    @JsonIgnoreProperties("airports")
    private CountryEntity country;

}
