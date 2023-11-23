package com.binhan.flightmanagement.service.impl;

import com.binhan.flightmanagement.converter.AirportConverter;
import com.binhan.flightmanagement.dto.AirportDto;
import com.binhan.flightmanagement.dto.CountryDto;
import com.binhan.flightmanagement.exception.AirportExistedException;
import com.binhan.flightmanagement.exception.AirportNotFoundException;
import com.binhan.flightmanagement.models.AirportEntity;
import com.binhan.flightmanagement.models.CountryEntity;
import com.binhan.flightmanagement.models.FileDataEntity;
import com.binhan.flightmanagement.models.FlightEntity;
import com.binhan.flightmanagement.repository.AirportRepository;
import com.binhan.flightmanagement.repository.FileDataRepository;
import com.binhan.flightmanagement.repository.FlightRepository;
import com.binhan.flightmanagement.repository.ReservationRepository;
import com.binhan.flightmanagement.service.AirportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class AirportServiceImpl implements AirportService {
    private AirportRepository airportRepository;
    private AirportConverter airportConverter;
    private FlightRepository flightRepository;
    private ReservationRepository reservationRepository;
    private FileDataRepository fileDataRepository;

    private final String FOLDER_PATH="D:\\capture";

    @Autowired
    public AirportServiceImpl(AirportRepository airportRepository,AirportConverter airportConverter,
                              FlightRepository flightRepository,ReservationRepository reservationRepository,
                              FileDataRepository fileDataRepository){
        this.fileDataRepository=fileDataRepository;
        this.airportRepository=airportRepository;
        this.reservationRepository=reservationRepository;
        this.flightRepository=flightRepository;
        this.airportConverter=airportConverter;
    }

    @Override
    public List<AirportEntity> findAll() {
        return airportRepository.findAll();
    }

    @Override
    public List<AirportDto> findAllAirports() {
        List<AirportEntity> airportEntities = airportRepository.findAll();
        List<AirportDto> airportDtos = airportEntities
                .stream()
                .map(item -> airportConverter.convertToDto(item))
                .collect(Collectors.toList());
        return airportDtos;
    }

    @Override
    public AirportEntity save(AirportDto airport) {
        AirportEntity airportEntity= airportConverter.convertToEntity(airport);
        if(airportRepository.existsByAirportNameAndCountry(airportEntity.getAirportName(),airportEntity.getCountry())){
            throw new AirportExistedException("Airport is already existed in database");
        }
        return airportRepository.save(airportEntity);
    }

    @Override
    public void saveAirportsByExcel(List<AirportEntity> airportEntities) {
        for (AirportEntity item:airportEntities) {
            Boolean check = airportRepository.existsByAirportName(item.getAirportName());
            if(check==false) {
                AirportEntity airport= AirportEntity.builder()
                        .airportName(item.getAirportName())
                        .location(item.getLocation())
                        .country(item.getCountry())
                        .build();
                airportRepository.saveAndFlush(airport);
            }

        }

    }

    @Override
    public void delete(Long id) {
        List<FlightEntity> flights1 = flightRepository.findByArrivalAirport_Id(id);
        List<FlightEntity> flights2 = flightRepository.findByDepartureAirport_Id(id);
        List<FlightEntity> flights =  Stream.concat(flights1.stream(), flights2.stream())
                .distinct()
                .collect(Collectors.toList());
        List<Long> ids = flights.stream().map(item->item.getId()).collect(Collectors.toList());
        for (Long item:ids){
            reservationRepository.deleteByFlight_Id(item);
        }
        flightRepository.deleteByIdIn(ids);
        airportRepository.deleteById(id);
    }
    @Override
    public String uploadImageToFileSystem(MultipartFile file) throws IOException {
        String filePath=FOLDER_PATH+file.getOriginalFilename();

        FileDataEntity fileData=fileDataRepository.save(FileDataEntity.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .filePath(filePath).build());

        file.transferTo(new File(filePath));

        if (fileData != null) {
            return "file uploaded successfully : " + filePath;
        }
        return null;
    }
    @Override
    public byte[] downloadImageFromFileSystem(String fileName) throws IOException {
        Optional<FileDataEntity> fileData = fileDataRepository.findByName(fileName);
        String filePath=fileData.get().getFilePath();
        byte[] images = Files.readAllBytes(new File(filePath).toPath());
        return images;
    }

    @Override
    public AirportEntity findById(Long id) {
        AirportEntity airport = airportRepository.findById(id).get();
        if(airport==null){
            throw new AirportNotFoundException("cant find airport");
        }
        return airport;
    }

    @Override
    public Page<AirportDto> findAirportsWithPaginationAndSorting(int offset, int pageSize, String field) {
        Page<AirportEntity> airportEntities;
        if (field == null) {
            airportEntities = airportRepository.findAll(PageRequest.of(offset, pageSize));
        } else {
            airportEntities = airportRepository.findAll(PageRequest.of(offset, pageSize).withSort(Sort.by(field)));
        }
        return airportEntities.map(item->airportConverter.convertToDto(item));
    }

    @Override
    public List<AirportDto> findAirportsWithSorting(String field) {
        List<AirportEntity> airportEntities;
        if(field == null){
            airportEntities = airportRepository.findAll();
        }else{
            airportEntities = airportRepository.findAll(Sort.by(Sort.Direction.ASC,field));
        }
        List<AirportDto> airportDtos = airportEntities.stream()
                .map(item->airportConverter.convertToDto(item))
                .collect(Collectors.toList());
        return airportDtos;
    }

}
