package com.automation.core.utils;

import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.exceptions.CsvException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Core utility for data-driven test support (CSV/Excel).
 * Centralizes test data retrieval for API/UI/data-driven testing.
 */
public class DataDrivenUtils {
    /**
     * Reads a CSV file and returns a list of maps (header -> value).
     */
    public static List<Map<String, String>> readCsvAsMap(String filePath) throws IOException, CsvException {
        try (Reader reader = Files.newBufferedReader(Paths.get(filePath));
             CSVReaderHeaderAware csvReader = new CSVReaderHeaderAware(reader)) {
            List<Map<String, String>> records = new ArrayList<>();
            Map<String, String> row;
            while ((row = csvReader.readMap()) != null) {
                records.add(row);
            }
            return records;
        }
    }

    /**
     * Reads an Excel file and returns a list of maps (header -> value) for the given sheet.
     */
    public static List<Map<String, String>> readExcelAsMap(String filePath, String sheetName) throws IOException {
        List<Map<String, String>> data = new ArrayList<>();
        try (InputStream inp = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(inp)) {
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) return data;
            Iterator<Row> rowIterator = sheet.iterator();
            if (!rowIterator.hasNext()) return data;
            Row headerRow = rowIterator.next();
            List<String> headers = new ArrayList<>();
            for (Cell cell : headerRow) {
                headers.add(cell.getStringCellValue());
            }
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Map<String, String> rowData = new LinkedHashMap<>();
                for (int i = 0; i < headers.size(); i++) {
                    Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    rowData.put(headers.get(i), cell.toString());
                }
                data.add(rowData);
            }
        }
        return data;
    }

    /**
     * Gets test data row by tc_id from CSV or Excel (sheetName required for Excel).
     */
    public static Map<String, String> getTestDataByTcId(String filePath, String tcId) throws IOException, CsvException {
        if (filePath.endsWith(".csv")) {
            List<Map<String, String>> rows = readCsvAsMap(filePath);
            for (Map<String, String> row : rows) {
                if (tcId.equals(row.get("tc_id"))) {
                    return row;
                }
            }
        } else if (filePath.endsWith(".xlsx")) {
            List<Map<String, String>> rows = readExcelAsMap(filePath, "Sheet1"); // default sheet
            for (Map<String, String> row : rows) {
                if (tcId.equals(row.get("tc_id"))) {
                    return row;
                }
            }
        }
        return null;
    }

    /**
     * Gets all test data rows matching a column value from CSV or Excel (sheetName required for Excel).
     */
    public static List<Map<String, String>> getTestDataRows(String filePath, String column, String value) throws IOException, CsvException {
        List<Map<String, String>> result = new ArrayList<>();
        if (filePath.endsWith(".csv")) {
            List<Map<String, String>> rows = readCsvAsMap(filePath);
            for (Map<String, String> row : rows) {
                if (value.equals(row.get(column))) {
                    result.add(row);
                }
            }
        } else if (filePath.endsWith(".xlsx")) {
            List<Map<String, String>> rows = readExcelAsMap(filePath, "Sheet1"); // default sheet
            for (Map<String, String> row : rows) {
                if (value.equals(row.get(column))) {
                    result.add(row);
                }
            }
        }
        return result;
    }
}

