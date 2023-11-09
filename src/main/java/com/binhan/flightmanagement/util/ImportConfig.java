package com.binhan.flightmanagement.util;

import com.binhan.flightmanagement.dto.AirportDto;
import com.binhan.flightmanagement.dto.CountryDto;
import com.binhan.flightmanagement.dto.FlightDto;
import com.binhan.flightmanagement.models.CountryEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImportConfig {
    private Integer sheetIndex;
    private Integer headerIndex;
    private Integer startRow;
    private Class dataClass;
    private List<CellConfig> cellImportConfigs;

    public static final ImportConfig countryImport;
    static{
        countryImport = new ImportConfig();
        countryImport.setSheetIndex(0);
        countryImport.setHeaderIndex(0);
        countryImport.setStartRow(1);
        countryImport.setDataClass(CountryDto.class);
        List<CellConfig> countryImportCellConfigs = new ArrayList<>();
        countryImportCellConfigs.add(new CellConfig(0, "countryName"));
        countryImport.setCellImportConfigs(countryImportCellConfigs);
    }
    public static final ImportConfig airportImport;
    static{
        airportImport = new ImportConfig();
        airportImport.setSheetIndex(1);
        airportImport.setHeaderIndex(0);
        airportImport.setStartRow(1);
        airportImport.setDataClass(AirportDto.class);
        List<CellConfig> airportImportCellConfigs = new ArrayList<>();
        airportImportCellConfigs.add(new CellConfig(0, "AirportName"));
        airportImportCellConfigs.add(new CellConfig(1, "Location"));
        airportImportCellConfigs.add(new CellConfig(2, "CountryId"));
        airportImport.setCellImportConfigs(airportImportCellConfigs);
    }
    public static final ImportConfig flightImport;
    static{
        flightImport = new ImportConfig();
        flightImport.setSheetIndex(2);
        flightImport.setHeaderIndex(0);
        flightImport.setStartRow(1);
        flightImport.setDataClass(FlightDto.class);
        List<CellConfig> flightImportCellConfigs = new ArrayList<>();
        flightImportCellConfigs.add(new CellConfig(0, "departureTime"));
        flightImportCellConfigs.add(new CellConfig(1, "arrivalTime"));
        flightImportCellConfigs.add(new CellConfig(2, "status"));
        flightImportCellConfigs.add(new CellConfig(3, "seats"));
        flightImportCellConfigs.add(new CellConfig(4, "ticketPrice"));
        flightImportCellConfigs.add(new CellConfig(5, "departureAirportId"));
        flightImportCellConfigs.add(new CellConfig(6, "arrivalAirportId"));
        flightImportCellConfigs.add(new CellConfig(7, "aircraftId"));
        flightImport.setCellImportConfigs(flightImportCellConfigs);
    }
}
