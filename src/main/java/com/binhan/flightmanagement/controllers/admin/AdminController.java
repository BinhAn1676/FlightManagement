package com.binhan.flightmanagement.controllers.admin;

import com.binhan.flightmanagement.dto.*;
import com.binhan.flightmanagement.dto.request.RegisterDto;
import com.binhan.flightmanagement.dto.request.RegisterRequest;
import com.binhan.flightmanagement.dto.response.APIResponse;
import com.binhan.flightmanagement.dto.response.BaseResponse;
import com.binhan.flightmanagement.models.PaymentEntity;
import com.binhan.flightmanagement.repository.FlightRepository;
import com.binhan.flightmanagement.service.*;
import com.binhan.flightmanagement.util.ExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    private UserService userService;

    private AdminService adminService;

    private CountryService countryService;
    private AirportService airportService;
    private FlightService flightService;
    private AircraftService aircraftService;
    private ReservationService reservationService;
    private PaymentService paymentService;

    @Autowired
    public AdminController(UserService userService, AdminService adminService,CountryService countryService,
                           AirportService airportService,FlightService flightService,AircraftService aircraftService,
                           ReservationService reservationService,PaymentService paymentService) {
        this.reservationService=reservationService;
        this.paymentService=paymentService;
        this.aircraftService=aircraftService;
        this.userService = userService;
        this.adminService = adminService;
        this.countryService=countryService;
        this.airportService=airportService;
        this.flightService=flightService;
    }

    /**
     * admin can add more user which can be employee
     *
     * @param newUser
     * @return
     */
    @PostMapping("/newUser")
    public ResponseEntity<?> addNewUser(@RequestBody RegisterRequest newUser) {
        String msg = userService.saveNewUser(newUser);
        if (msg == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Tài khoản đã tồn tại");
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(msg);
        }
    }



    @GetMapping("/user/filter")
    private ResponseEntity<?> getUsersWithSort(@RequestParam(value = "field",required = false) String field) {
        List<UserDto> userDtos = adminService.findUsersWithSorting(field);
        return ResponseEntity.status(HttpStatus.OK).body(new APIResponse<>(userDtos.size(), userDtos));
    }


    @GetMapping("/user/paginationAndSort")
    private ResponseEntity<?> getUsersWithPaginationAndSort(@RequestParam("offset") int offset,
                                                            @RequestParam("pageSize") int pageSize,
                                                            @RequestParam(value = "field",required = false) String field) {
        Page<UserDto> usersPaging = adminService.findUsersWithPaginationAndSorting(offset, pageSize, field);
        return ResponseEntity.status(HttpStatus.OK).body(new APIResponse<>(usersPaging.getSize(), usersPaging));
    }

    @PostMapping("import")
    public ResponseEntity<BaseResponse> importData(@RequestParam("file")MultipartFile importFile){
        BaseResponse baseResponse = adminService.importData(importFile);
        return ResponseEntity.ok(baseResponse);
    }

    @GetMapping("/export")
    public ResponseEntity<Resource> exportData() throws Exception {
        List<CountryDto> countryDtos = countryService.findAllCountries();
        List<AirportDto> airportDtos = airportService.findAllAirports();
        List<FlightDto> flightDtos = flightService.findAllFlights();
        List<AircraftDto> aircraftDtos = aircraftService.findAll();
        if (!CollectionUtils.isEmpty(countryDtos)) {
            String fileName = "Data export" + ".xlsx";

            ByteArrayInputStream in = ExcelUtils.exportToExcel(countryDtos,airportDtos,flightDtos,aircraftDtos, fileName);

            InputStreamResource inputStreamResource = new InputStreamResource(in);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8)
                    )
                    .contentType(MediaType.parseMediaType("application/vnd.ms-excel; charset=UTF-8"))
                    .body(inputStreamResource);
        } else {
            throw new Exception("No data");
        }
    }

    @GetMapping("/reservation")
    public ResponseEntity<?> getAllReservations() {
        List<ReservationDto> reservationDtos = reservationService.findAllReservations();
        return ResponseEntity.ok(reservationDtos);
    }

    @GetMapping("/reservation/filter")
    private ResponseEntity<?> getReservationsWithSort(@RequestParam(value = "field",required = false) String field) {
        List<ReservationDto> reservationDtos = adminService.findReservationsWithSorting(field);
        return ResponseEntity.status(HttpStatus.OK).body(new APIResponse<>(reservationDtos.size(), reservationDtos));
    }


    @GetMapping("/reservation/paginationAndSort")
    private ResponseEntity<?> getReservationsWithPaginationAndSort(@RequestParam("offset") int offset,
                                                            @RequestParam("pageSize") int pageSize,
                                                            @RequestParam(value = "field",required = false) String field) {
        Page<ReservationDto> reservationDtos = adminService.findReservationsWithPaginationAndSorting(offset, pageSize, field);
        return ResponseEntity.status(HttpStatus.OK).body(new APIResponse<>(reservationDtos.getSize(), reservationDtos));
    }

    @GetMapping("/payment")
    public ResponseEntity<?> getAllPayments() {
        List<PaymentEntity> paymentEntities = paymentService.findAllPayments();
        return ResponseEntity.ok(paymentEntities);
    }

    @GetMapping("/payment/filter")
    private ResponseEntity<?> getPaymentsWithSort(@RequestParam(value = "field",required = false) String field) {
        List<PaymentEntity> paymentEntities = paymentService.findPaymentsWithSorting(field);
        return ResponseEntity.status(HttpStatus.OK).body(new APIResponse<>(paymentEntities.size(), paymentEntities));
    }


    @GetMapping("/payment/paginationAndSort")
    private ResponseEntity<?> getPaymentsWithPaginationAndSort(@RequestParam("offset") int offset,
                                                                   @RequestParam("pageSize") int pageSize,
                                                                   @RequestParam(value = "field",required = false) String field) {
        Page<PaymentEntity> paymentEntities = paymentService.findPaymentsWithPaginationAndSorting(offset, pageSize, field);
        return ResponseEntity.status(HttpStatus.OK).body(new APIResponse<>(paymentEntities.getSize(), paymentEntities));
    }
}
