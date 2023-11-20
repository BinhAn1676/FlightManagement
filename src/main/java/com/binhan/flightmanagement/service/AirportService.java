package com.binhan.flightmanagement.service;

import com.binhan.flightmanagement.dto.AirportDto;
import com.binhan.flightmanagement.models.AirportEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AirportService {
    List<AirportEntity> findAll();
    List<AirportDto> findAllAirports();

    AirportEntity save(AirportDto airport);

    void saveAirportsByExcel(List<AirportEntity> airportEntities);

    void delete(Long id);

    String uploadImageToFileSystem(MultipartFile file) throws IOException;

    byte[] downloadImageFromFileSystem(String fileName) throws IOException;
}
