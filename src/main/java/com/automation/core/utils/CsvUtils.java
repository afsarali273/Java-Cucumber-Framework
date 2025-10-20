package com.automation.core.utils;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.exceptions.CsvException;
import com.opencsv.ICSVWriter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;

/**
 * Utility class for common CSV operations using OpenCSV.
 */
public class CsvUtils {
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
     * Writes a list of maps to a CSV file (header -> value).
     */
    public static void writeCsvFromMap(String filePath, List<Map<String, String>> data) throws IOException {
        if (data.isEmpty()) return;
        try (Writer writer = Files.newBufferedWriter(Paths.get(filePath));
             CSVWriter csvWriter = new CSVWriter(writer)) {
            Set<String> headers = data.get(0).keySet();
            csvWriter.writeNext(headers.toArray(new String[0]));
            for (Map<String, String> row : data) {
                String[] values = headers.stream().map(h -> row.getOrDefault(h, "")).toArray(String[]::new);
                csvWriter.writeNext(values);
            }
        }
    }

    /**
     * Appends a row to an existing CSV file.
     */
    public static void appendRow(String filePath, Map<String, String> row) throws IOException, CsvException {
        List<Map<String, String>> existing = readCsvAsMap(filePath);
        existing.add(row);
        writeCsvFromMap(filePath, existing);
    }

    /**
     * Searches for rows matching a condition.
     */
    public static List<Map<String, String>> searchRows(String filePath, String column, String value) throws IOException, CsvException {
        List<Map<String, String>> all = readCsvAsMap(filePath);
        List<Map<String, String>> result = new ArrayList<>();
        for (Map<String, String> row : all) {
            if (value.equals(row.get(column))) {
                result.add(row);
            }
        }
        return result;
    }

    /**
     * Updates rows matching a condition.
     */
    public static void updateRows(String filePath, String column, String value, Map<String, String> newValues) throws IOException, CsvException {
        List<Map<String, String>> all = readCsvAsMap(filePath);
        for (Map<String, String> row : all) {
            if (value.equals(row.get(column))) {
                row.putAll(newValues);
            }
        }
        writeCsvFromMap(filePath, all);
    }

    /**
     * Reads CSV with custom delimiter.
     */
    public static List<String[]> readCsvWithDelimiter(String filePath, char delimiter) throws IOException, CsvException {
        try (Reader reader = Files.newBufferedReader(Paths.get(filePath));
             CSVReader csvReader = new CSVReaderBuilder(reader)
                     .withCSVParser(new com.opencsv.CSVParserBuilder().withSeparator(delimiter).build())
                     .build()) {
            return csvReader.readAll();
        }
    }

    /**
     * Reads a CSV file and returns all rows as List<List<String>> (no header mapping).
     */
    public static List<List<String>> readCsvAsList(String filePath) throws IOException, CsvException {
        try (Reader reader = Files.newBufferedReader(Paths.get(filePath));
             CSVReader csvReader = new CSVReader(reader)) {
            List<List<String>> rows = new ArrayList<>();
            for (String[] row : csvReader.readAll()) {
                rows.add(Arrays.asList(row));
            }
            return rows;
        }
    }

    /**
     * Gets all values from a specific column by name.
     */
    public static List<String> getColumnValues(String filePath, String columnName) throws IOException, CsvException {
        List<Map<String, String>> rows = readCsvAsMap(filePath);
        List<String> values = new ArrayList<>();
        for (Map<String, String> row : rows) {
            values.add(row.getOrDefault(columnName, ""));
        }
        return values;
    }

    /**
     * Gets a row by index (as Map<String, String>).
     */
    public static Map<String, String> getRowByIndex(String filePath, int index) throws IOException, CsvException {
        List<Map<String, String>> rows = readCsvAsMap(filePath);
        if (index < 0 || index >= rows.size()) return null;
        return rows.get(index);
    }

    /**
     * Converts CSV to JSON Array (for API payloads).
     */
    public static JsonArray csvToJson(String filePath) throws IOException, CsvException {
        List<Map<String, String>> rows = readCsvAsMap(filePath);
        Gson gson = new Gson();
        JsonArray array = new JsonArray();
        for (Map<String, String> row : rows) {
            JsonObject obj = gson.toJsonTree(row).getAsJsonObject();
            array.add(obj);
        }
        return array;
    }

    /**
     * Validates CSV structure: header presence, row count, column count.
     */
    public static boolean validateCsv(String filePath, List<String> requiredHeaders, int minRows, int minColumns) throws IOException, CsvException {
        List<Map<String, String>> rows = readCsvAsMap(filePath);
        if (rows.size() < minRows) return false;
        Set<String> headers = rows.isEmpty() ? Collections.emptySet() : rows.get(0).keySet();
        if (!headers.containsAll(requiredHeaders)) return false;
        for (Map<String, String> row : rows) {
            if (row.size() < minColumns) return false;
        }
        return true;
    }

    /**
     * Filters rows by predicate (lambda).
     */
    public static List<Map<String, String>> filterRows(String filePath, Predicate<Map<String, String>> predicate) throws IOException, CsvException {
        List<Map<String, String>> rows = readCsvAsMap(filePath);
        List<Map<String, String>> result = new ArrayList<>();
        for (Map<String, String> row : rows) {
            if (predicate.test(row)) {
                result.add(row);
            }
        }
        return result;
    }

    /**
     * Gets distinct values in a column.
     */
    public static Set<String> getDistinctColumnValues(String filePath, String columnName) throws IOException, CsvException {
        List<String> values = getColumnValues(filePath, columnName);
        return new HashSet<>(values);
    }

    /**
     * Merges multiple CSV files (same header) into one List<Map<String, String>>.
     */
    public static List<Map<String, String>> mergeCsvFiles(List<String> filePaths) throws IOException, CsvException {
        List<Map<String, String>> merged = new ArrayList<>();
        for (String path : filePaths) {
            merged.addAll(readCsvAsMap(path));
        }
        return merged;
    }

    /**
     * Splits a CSV file into chunks by row count.
     */
    public static List<List<Map<String, String>>> splitCsvByRows(String filePath, int chunkSize) throws IOException, CsvException {
        List<Map<String, String>> rows = readCsvAsMap(filePath);
        List<List<Map<String, String>>> chunks = new ArrayList<>();
        for (int i = 0; i < rows.size(); i += chunkSize) {
            chunks.add(rows.subList(i, Math.min(i + chunkSize, rows.size())));
        }
        return chunks;
    }

    /**
     * Selects a random row (for random test data).
     */
    public static Map<String, String> getRandomRow(String filePath) throws IOException, CsvException {
        List<Map<String, String>> rows = readCsvAsMap(filePath);
        if (rows.isEmpty()) return null;
        Random rand = new Random();
        return rows.get(rand.nextInt(rows.size()));
    }

    /**
     * Maps CSV rows to POJO list (using reflection, basic version).
     */
    public static <T> List<T> mapCsvToPojo(String filePath, Class<T> clazz) throws IOException, CsvException {
        List<Map<String, String>> rows = readCsvAsMap(filePath);
        List<T> result = new ArrayList<>();
        Gson gson = new Gson();
        for (Map<String, String> row : rows) {
            String json = gson.toJson(row);
            result.add(gson.fromJson(json, clazz));
        }
        return result;
    }

    /**
     * Writes CSV with custom delimiter.
     */
    public static void writeCsvWithDelimiter(String filePath, List<String[]> data, char delimiter) throws IOException {
        try (Writer writer = Files.newBufferedWriter(Paths.get(filePath));
             ICSVWriter csvWriter = new com.opencsv.CSVWriterBuilder(writer)
                     .withSeparator(delimiter)
                     .build()) {
            for (String[] row : data) {
                csvWriter.writeNext(row);
            }
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
