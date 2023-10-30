package com.binhan.flightmanagement.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "countries")
public class CountryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String countryName;

    @OneToMany(mappedBy = "country",fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonIgnoreProperties("country")
    private List<AirportEntity> airports = new ArrayList<>();
}
