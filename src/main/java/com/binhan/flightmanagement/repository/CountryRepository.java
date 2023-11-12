package com.binhan.flightmanagement.repository;

import com.binhan.flightmanagement.models.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<CountryEntity,Long> {
    CountryEntity findByCountryName(String name);
    Boolean existsByCountryName(String name);
}
