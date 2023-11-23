package com.binhan.flightmanagement.service.impl;

import com.binhan.flightmanagement.converter.*;
import com.binhan.flightmanagement.dto.*;
import com.binhan.flightmanagement.dto.response.BaseResponse;
import com.binhan.flightmanagement.models.*;
import com.binhan.flightmanagement.repository.*;
import com.binhan.flightmanagement.service.*;
import com.binhan.flightmanagement.util.ExcelUtils;
import com.binhan.flightmanagement.util.FileFactory;
import com.binhan.flightmanagement.util.ImportConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    private UserRepository userRepository;
    private UserConverter userConverter;
    private CountryRepository countryRepository;
    private AirportRepository airportRepository;
    private FlightRepository flightRepository;

    private CountryConverter countryConverter;
    private AirportConverter airportConverter;
    private FlightConverter flightConverter;
    private AircraftConverter aircraftConverter;
    private AircraftRepository aircraftRepository;
    private CountryService countryService;
    private AirportService airportService;
    private FlightService flightService;
    private AircraftService aircraftService;
    private ReservationRepository reservationRepository;
    private ReservationConverter reservationConverter;

    @Autowired
    public AdminServiceImpl(UserRepository userRepository, UserConverter userConverter,
                            CountryRepository countryRepository, AirportRepository airportRepository,
                            FlightRepository flightRepository, CountryConverter countryConverter,
                            AirportConverter airportConverter, FlightConverter flightConverter,
                            AircraftConverter aircraftConverter, AircraftRepository aircraftRepository,
                            CountryService countryService, AirportService airportService,
                            FlightService flightService, AircraftService aircraftService,
                            ReservationRepository reservationRepository,ReservationConverter reservationConverter){
        this.reservationConverter=reservationConverter;
        this.reservationRepository=reservationRepository;
        this.flightService=flightService;
        this.aircraftService=aircraftService;
        this.airportService=airportService;
        this.aircraftRepository=aircraftRepository;
        this.countryService= countryService;
        this.aircraftConverter=aircraftConverter;
        this.airportRepository=airportRepository;
        this.countryRepository=countryRepository;
        this.flightRepository=flightRepository;
        this.userRepository=userRepository;
        this.userConverter=userConverter;
        this.countryConverter=countryConverter;
        this.airportConverter=airportConverter;
        this.flightConverter=flightConverter;
    }

    @Override
    public List<UserDto> findAll() {
        List<UserEntity> userEntities = userRepository.findAll();
        List<UserDto> userDtos = new ArrayList<>();
        for(var item : userEntities){
            UserDto userDto = userConverter.convertToDto(item);
            userDtos.add(userDto);
        }
        return userDtos;
    }

    @Override
    public List<UserDto> findUsersWithSorting(String field) {
        List<UserEntity> userEntities;
        if(field == null){
            userEntities = userRepository.findAll();
        }else{
            userEntities = userRepository.findAll(Sort.by(Sort.Direction.ASC,field));
        }
        List<UserDto> userDtos = new ArrayList<>();
        for(var item : userEntities){
            UserDto userDto = userConverter.convertToDto(item);
            userDtos.add(userDto);
        }
        return userDtos;
    }

    @Override
    public Page<UserDto> findUsersWithPagination(int offset, int pageSize) {
        Page<UserEntity> userEntities = userRepository.findAll(PageRequest.of(offset, pageSize));
        Page<UserDto> userDtos = userEntities.map(user -> {
            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setUserName(user.getUserName());
            userDto.setEmail(user.getEmail());
            userDto.setFullName(user.getFullName());
            userDto.setPhone(user.getPhone());
            userDto.setPassword(user.getPassword());
            return userDto;
        });
        return userDtos;
    }

    @Override
    public Page<UserDto> findUsersWithPaginationAndSorting(int offset, int pageSize, String field) {
        Page<UserEntity> userEntities;

        if (field == null) {
            userEntities = userRepository.findAll(PageRequest.of(offset, pageSize));
        } else {
            userEntities = userRepository.findAll(PageRequest.of(offset, pageSize).withSort(Sort.by(field)));
        }

        return userEntities.map(this::mapToUserDto);
    }

    @Override
    public BaseResponse importData(MultipartFile importFile) {
        BaseResponse baseResponse = new BaseResponse();
        var workbook = FileFactory.getWorkbookStream(importFile);
        //get data from workbook
        List<CountryDto> countryDtos = ExcelUtils.getImportData(workbook, ImportConfig.countryImport);
        List<AirportDto> airportDtos = ExcelUtils.getImportData(workbook,ImportConfig.airportImport);
        List<FlightDto> flightDtos = ExcelUtils.getImportData(workbook,ImportConfig.flightImport);
        List<AircraftDto> aircraftDtos = ExcelUtils.getImportData(workbook,ImportConfig.aircraftImport);
        //convert to Entity and save
        List<CountryEntity> countryEntities = countryDtos
                .stream()
                .map(item -> countryConverter.convertToEntity(item))
                .collect(Collectors.toList());
        countryService.saveCountríeByExcel(countryEntities);

        List<AirportEntity> airportEntities = airportDtos.stream()
                .map(item -> airportConverter.convertToEntity(item))
                .collect(Collectors.toList());
        airportService.saveAirportsByExcel(airportEntities);

        List<FlightEntity> flightEntities = flightDtos
                .stream()
                .map(item -> flightConverter.convertToEntityFromExcel(item))
                .collect(Collectors.toList());
        flightService.saveFlightsByExcel(flightEntities);

        List<AircraftEntity> aircraftEntities = aircraftDtos
                .stream()
                .map(item -> aircraftConverter.convertToEntity(item))
                .collect(Collectors.toList());
        aircraftService.saveAircraftsByExcel(aircraftEntities);

        baseResponse.setCode(String.valueOf(HttpStatus.OK));
        baseResponse.setMessage("Import successfully");
        return baseResponse;
    }

    @Override
    public Page<ReservationDto> findReservationsWithPaginationAndSorting(int offset, int pageSize, String field) {
        Page<ReservationEntity> reservationEntities;
        if (field == null) {
            reservationEntities = reservationRepository.findAll(PageRequest.of(offset, pageSize));
        } else {
            reservationEntities = reservationRepository.findAll(PageRequest.of(offset, pageSize).withSort(Sort.by(field)));
        }
        return reservationEntities.map(item->reservationConverter.convertToDto(item));
    }

    @Override
    public List<ReservationDto> findReservationsWithSorting(String field) {
        List<ReservationEntity> reservationEntities;
        if(field == null){
            reservationEntities = reservationRepository.findAll();
        }else{
            reservationEntities = reservationRepository.findAll(Sort.by(Sort.Direction.ASC,field));
        }
        List<ReservationDto> reservationDtos = reservationEntities.stream()
                .map(item->reservationConverter.convertToDto(item))
                .collect(Collectors.toList());
        return reservationDtos;
    }


    private UserDto mapToUserDto(UserEntity user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUserName(user.getUserName());
        userDto.setEmail(user.getEmail());
        userDto.setFullName(user.getFullName());
        userDto.setPhone(user.getPhone());
        userDto.setPassword(user.getPassword());
        return userDto;
    }
}
