package com.binhan.flightmanagement.util;

import com.binhan.flightmanagement.models.AirportEntity;
import com.binhan.flightmanagement.models.CountryEntity;
import com.binhan.flightmanagement.repository.AirportRepository;
import com.binhan.flightmanagement.repository.CountryRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.AccessType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Component
public class ExcelUpload {

    private CountryRepository countryRepository;

    @Autowired
    public ExcelUpload(CountryRepository countryRepository){
        this.countryRepository=countryRepository;
    }

    public boolean isValidExcelFile(MultipartFile file){
        return Objects.equals(file.getContentType(), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" );
    }

    public List<CountryEntity> getCountriesDataFromExcel(InputStream inputStream){
        List<CountryEntity> countryEntities = new ArrayList<>();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheet("Country");
            int rowIndex = 0;
            for(Row row : sheet ){
                if(rowIndex==0){
                    rowIndex++;
                    continue;
                }
                Iterator<Cell> cellIterator= row.iterator();
                int cellIndex = 0;
                CountryEntity countryEntity = new CountryEntity();
                while(cellIterator.hasNext()){
                    Cell cell = cellIterator.next();
                    switch (cellIndex){
                        case 0 -> countryEntity.setCountryName(cell.getStringCellValue());
                        default -> {
                        }
                    }
                    cellIndex++;
                }
                countryEntities.add(countryEntity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return countryEntities;
    }

    public List<AirportEntity> getAirportsDataFromExcel(InputStream inputStream) throws IOException {
        List<AirportEntity> airportEntities = new ArrayList<>();
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheet("Airport");
        int rowIndex =0;
        for(Row row : sheet){
            if(rowIndex==0){
                rowIndex++;
                continue;
            }
            Iterator<Cell> cellIterator = row.iterator();
            int cellIndex=0;
            AirportEntity airportEntity = new AirportEntity();
            while(cellIterator.hasNext()){
                Cell cell = cellIterator.next();
                switch (cellIndex){
                    case 0 -> airportEntity.setAirportName(cell.getStringCellValue());
                    case 1 -> airportEntity.setLocation(cell.getStringCellValue());
                    case 2 -> {
                        airportEntity.setCountry(countryRepository.findByCountryName(cell.getStringCellValue()));
                    }
                    default -> {
                    }
                }
                cellIndex++;
            }
            airportEntities.add(airportEntity);
        }
        return airportEntities;
    }
}
