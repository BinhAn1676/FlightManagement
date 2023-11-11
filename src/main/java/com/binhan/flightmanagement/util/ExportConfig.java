package com.binhan.flightmanagement.util;

import com.binhan.flightmanagement.dto.AircraftDto;
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
        countryCellConfig.add(new CellConfig(0, "id"));
        countryCellConfig.add(new CellConfig(1, "countryName"));
        countryExport.setCellExportConfigList(countryCellConfig);
    }

    public static final ExportConfig airportExport;
    static{
        airportExport = new ExportConfig();
        airportExport.setSheetIndex(1);
        airportExport.setStartRow(1);
        airportExport.setDataClazz(AirportDto.class);
        List<CellConfig> airportExportCellConfigs = new ArrayList<>();
        airportExportCellConfigs.add(new CellConfig(0, "id"));
        airportExportCellConfigs.add(new CellConfig(1, "airportName"));
        airportExportCellConfigs.add(new CellConfig(2, "location"));
        airportExportCellConfigs.add(new CellConfig(3, "countryName"));
        airportExport.setCellExportConfigList(airportExportCellConfigs);
    }
    public static final ExportConfig flightExport;
    static{
        flightExport = new ExportConfig();
        flightExport.setSheetIndex(2);
        flightExport.setStartRow(1);
        flightExport.setDataClazz(FlightDto.class);
        List<CellConfig> flightExportCellConfigs = new ArrayList<>();
        flightExportCellConfigs.add(new CellConfig(0, "id"));
        flightExportCellConfigs.add(new CellConfig(1, "departureTime"));
        flightExportCellConfigs.add(new CellConfig(2, "arrivalTime"));
        flightExportCellConfigs.add(new CellConfig(3, "status"));
        flightExportCellConfigs.add(new CellConfig(4, "seats"));
        flightExportCellConfigs.add(new CellConfig(5, "ticketPrice"));
        flightExportCellConfigs.add(new CellConfig(6, "departureAirport"));
        flightExportCellConfigs.add(new CellConfig(7, "arrivalAirport"));
        flightExportCellConfigs.add(new CellConfig(8, "aircraftId"));
        flightExport.setCellExportConfigList(flightExportCellConfigs);
    }
    public static final ExportConfig aircraftExport;
    static{
        aircraftExport= new ExportConfig();
        aircraftExport.setSheetIndex(3);
        aircraftExport.setStartRow(1);
        aircraftExport.setDataClazz(AircraftDto.class);
        List<CellConfig> aircraftExportCellConfigs = new ArrayList<>();
        aircraftExportCellConfigs.add(new CellConfig(0, "id"));
        aircraftExportCellConfigs.add(new CellConfig(1, "aircraftName"));
        aircraftExportCellConfigs.add(new CellConfig(2, "seats"));
        aircraftExportCellConfigs.add(new CellConfig(3, "type"));
        aircraftExport.setCellExportConfigList(aircraftExportCellConfigs);
    }
}
