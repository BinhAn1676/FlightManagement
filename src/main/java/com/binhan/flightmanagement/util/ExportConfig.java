package com.binhan.flightmanagement.util;

import com.binhan.flightmanagement.dto.AirportDto;
import com.binhan.flightmanagement.dto.CountryDto;
import com.binhan.flightmanagement.dto.FlightDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExportConfig {
    private int sheetIndex;

    private int startRow;

    private Class dataClazz;

    private List<CellConfig> cellExportConfigList;

    public static final ExportConfig countryExport;
    static{
        countryExport = new ExportConfig();
        countryExport.setSheetIndex(0);
        countryExport.setStartRow(1);
        countryExport.setDataClazz(CountryDto.class);
        List<CellConfig> countryCellConfig = new ArrayList<>();
        countryCellConfig.add(new CellConfig(0, "countryName"));
        countryExport.setCellExportConfigList(countryCellConfig);
    }

    public static final ExportConfig airportExport;
    static{
        airportExport = new ExportConfig();
        airportExport.setSheetIndex(1);
        airportExport.setStartRow(1);
        airportExport.setDataClazz(AirportDto.class);
        List<CellConfig> airportImportCellConfigs = new ArrayList<>();
        airportImportCellConfigs.add(new CellConfig(0, "AirportName"));
        airportImportCellConfigs.add(new CellConfig(1, "Location"));
        airportImportCellConfigs.add(new CellConfig(2, "CountryId"));
        airportExport.setCellExportConfigList(airportImportCellConfigs);
    }
    public static final ExportConfig flightExport;
    static{
        flightExport = new ExportConfig();
        flightExport.setSheetIndex(2);
        flightExport.setStartRow(1);
        flightExport.setDataClazz(FlightDto.class);
        List<CellConfig> flightImportCellConfigs = new ArrayList<>();
        flightImportCellConfigs.add(new CellConfig(0, "departureTime"));
        flightImportCellConfigs.add(new CellConfig(1, "arrivalTime"));
        flightImportCellConfigs.add(new CellConfig(2, "status"));
        flightImportCellConfigs.add(new CellConfig(3, "seats"));
        flightImportCellConfigs.add(new CellConfig(4, "ticketPrice"));
        flightImportCellConfigs.add(new CellConfig(5, "departureAirportId"));
        flightImportCellConfigs.add(new CellConfig(6, "arrivalAirportId"));
        flightImportCellConfigs.add(new CellConfig(7, "aircraftId"));
        flightExport.setCellExportConfigList(flightImportCellConfigs);
    }
}
