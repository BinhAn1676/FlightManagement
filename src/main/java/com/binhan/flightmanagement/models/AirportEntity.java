package com.binhan.flightmanagement.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

    public AirportEntity(String airportName, String location, CountryEntity country) {
        this.airportName = airportName;
        this.location = location;
        this.country = country;
    }
}
