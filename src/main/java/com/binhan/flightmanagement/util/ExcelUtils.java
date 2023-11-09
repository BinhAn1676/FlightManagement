package com.binhan.flightmanagement.util;

import com.binhan.flightmanagement.dto.AirportDto;
import com.binhan.flightmanagement.dto.CountryDto;
import com.binhan.flightmanagement.dto.FlightDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.util.List;

import static com.binhan.flightmanagement.util.FileFactory.PATH_TEMPLATE;

@Component
@Slf4j
public class ExcelUtils {

    public static ByteArrayInputStream exportToExcel(List<CountryDto> countryDtos, List<AirportDto>  airportDtos,
                                                     List<FlightDto> flightDtos,String fileName) throws IOException {
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
        File file;
        FileInputStream fileInputStream;
        try {
            file= ResourceUtils.getFile(PATH_TEMPLATE + fileName);
            fileInputStream = new FileInputStream(file);
        } catch (Exception e) {
            log.info("FILE NOT FOUND");
            file = FileFactory.createFile(fileName,xssfWorkbook);
            fileInputStream= new FileInputStream(file);
        }
        //create freeze pane in excel file
        XSSFSheet countrySheet = xssfWorkbook.createSheet("country");
        //countrySheet.createFreezePane(4, 2, 4, 2);
        countrySheet.createFreezePane(0, 1, 0, 1);


        XSSFSheet airportSheet = xssfWorkbook.createSheet("airport");
        //airportSheet.createFreezePane(4, 2, 4, 2);
        airportSheet.createFreezePane(0, 1, 0, 1);


        XSSFSheet flightSheet = xssfWorkbook.createSheet("flight");
        //flightSheet.createFreezePane(4, 2, 4, 2);
        flightSheet.createFreezePane(0,1,0,1);


        //create font for title
        XSSFFont titleFont=xssfWorkbook.createFont();
        titleFont.setFontName("Arial");
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 14);

        //create style for cell of title and apply font to cell
        XSSFCellStyle titleCellStyle = xssfWorkbook.createCellStyle();
        titleCellStyle.setAlignment(HorizontalAlignment.CENTER);
        titleCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        titleCellStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.index);
        titleCellStyle.setBorderBottom(BorderStyle.MEDIUM);
        titleCellStyle.setBorderLeft(BorderStyle.MEDIUM);
        titleCellStyle.setBorderRight(BorderStyle.MEDIUM);
        titleCellStyle.setBorderTop(BorderStyle.MEDIUM);
        titleCellStyle.setFont(titleFont);
        titleCellStyle.setWrapText(true);

        //create font for data
        XSSFFont dataFont = xssfWorkbook.createFont();
        dataFont.setFontName("Arial");
        dataFont.setBold(false);
        dataFont.setFontHeightInPoints((short) 10);

        //create style for cell data and apply font data to cell
        XSSFCellStyle dataCellStyle = xssfWorkbook.createCellStyle();
        dataCellStyle.setAlignment(HorizontalAlignment.CENTER);
        dataCellStyle.setBorderBottom(BorderStyle.THIN);
        dataCellStyle.setBorderLeft(BorderStyle.THIN);
        dataCellStyle.setBorderRight(BorderStyle.THIN);
        dataCellStyle.setBorderTop(BorderStyle.THIN);
        dataCellStyle.setFont(dataFont);
        dataCellStyle.setWrapText(true);

        insertFieldNameToWorkbook(ExportConfig.countryExport.getCellExportConfigList(),countrySheet,titleCellStyle);
        insertFieldNameToWorkbook(ExportConfig.airportExport.getCellExportConfigList(),countrySheet,titleCellStyle);
        insertFieldNameToWorkbook(ExportConfig.flightExport.getCellExportConfigList(),countrySheet,titleCellStyle);

        //insert data of fieldName to excel
        insertDataToWorkbook(xssfWorkbook, ExportConfig.countryExport, countryDtos, dataCellStyle);

        //insertDataToWorkbook(xssfWorkbook, ExportConfig.airportExport, airportDtos, dataCellStyle);

        //insertDataToWorkbook(xssfWorkbook, ExportConfig.flightExport, flightDtos, dataCellStyle);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        xssfWorkbook.write(outputStream);
        outputStream.close();
        fileInputStream.close();
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    private static <T> void insertDataToWorkbook(Workbook workbook,ExportConfig exportConfig,List<T> datas,
                                                 XSSFCellStyle xssfCellStyle){
        int startRowIndex = exportConfig.getStartRow();//2

        int sheetIndex = exportConfig.getSheetIndex();//1

        Class clazz = exportConfig.getDataClazz();

        List<CellConfig> cellConfigs = exportConfig.getCellExportConfigList();

        Sheet sheet = workbook.getSheetAt(sheetIndex);

        int currentRowIndex = startRowIndex;

        for (T data : datas) {
            Row currentRow = sheet.getRow(currentRowIndex);
            if (ObjectUtils.isEmpty(currentRow)) {
                currentRow = sheet.createRow(currentRowIndex);
            }
            //insert data to row
            insertDataToCell(data, currentRow, cellConfigs, clazz, sheet, xssfCellStyle);
            currentRowIndex++;
        }
    }

    private static <T> void insertDataToCell(T data, Row currentRow, List<CellConfig> cellConfigs, Class clazz,
                                             Sheet sheet, XSSFCellStyle xssfCellStyle) {
        for(CellConfig cellConfig : cellConfigs){
            Cell currentCell = currentRow.getCell(cellConfig.getColumnIndex());
            if(ObjectUtils.isEmpty(currentCell)){
                currentCell=currentRow.createCell(cellConfig.getColumnIndex());
            }
            //get data for cell
            String cellValue = getCellValue(data, cellConfig, clazz);

            //set data
            currentCell.setCellValue(cellValue);
            sheet.autoSizeColumn(cellConfig.getColumnIndex());
            currentCell.setCellStyle(xssfCellStyle);
        }
    }
    private static <T> String getCellValue(T data, CellConfig cellConfig, Class clazz) {
        String fieldName = cellConfig.getFieldName();
        try {
            Field field = getDeclaredField(clazz, fieldName);
            if (!ObjectUtils.isEmpty(field)) {
                field.setAccessible(true);
                return !ObjectUtils.isEmpty(field.get(data)) ? field.get(data).toString() : "";
            }
            return "";
        } catch (Exception e) {
            log.info("" + e);
            return "";
        }
    }
    private static Field getDeclaredField(Class clazz, String fieldName) {
        if (ObjectUtils.isEmpty(clazz) || ObjectUtils.isEmpty(fieldName)) {
            return null;
        }
        do {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field;
            } catch (Exception e) {
                log.info("" + e);
            }
        } while ((clazz = clazz.getSuperclass()) != null);

        return null;
    }

    private static <T> void insertFieldNameToWorkbook(List<CellConfig> cellConfigs,
                                                      Sheet sheet,
                                                      XSSFCellStyle titleCellStyle){
        //title -> first row of excel -> get top row
        int currentRow = sheet.getTopRow();

        //create row
        Row row = sheet.createRow(currentRow);
        int i=0;

        //resize fix text in each cell
        sheet.autoSizeColumn(currentRow);

        //insert field name to cell
        for(CellConfig cellConfig:cellConfigs){
            Cell currentCell = row.createCell(i);
            String fieldName = cellConfig.getFieldName();
            currentCell.setCellValue(fieldName);
            currentCell.setCellStyle(titleCellStyle);
            sheet.autoSizeColumn(i);
            i++;
        }
    }

}
