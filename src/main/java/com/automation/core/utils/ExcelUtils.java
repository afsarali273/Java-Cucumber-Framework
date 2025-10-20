package com.automation.core.utils;


import com.automation.core.interfaces.excel.Column;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Generic Excel Parser supporting:
 * 1. DTO Mapping via @Column annotation
 * 2. Dynamic header-based parsing via column names list
 */
public class ExcelUtils {

    /**
     * ✅ Parse Excel into List<T> using @Column annotations in DTO class
     */
    public static <T> List<T> parseExcel(String filePath, Class<T> dtoClass) throws Exception {
        List<T> resultList = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            if (!rowIterator.hasNext()) return resultList;

            // Read header row
            Row headerRow = rowIterator.next();
            Map<String, Integer> headerIndexMap = getHeaderIndexMap(headerRow);

            // Map fields -> Excel columns
            Map<Field, Integer> fieldColumnMap = mapFieldsToColumns(dtoClass, headerIndexMap);

            // Populate DTOs
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                T dto = dtoClass.getDeclaredConstructor().newInstance();

                for (Map.Entry<Field, Integer> entry : fieldColumnMap.entrySet()) {
                    Field field = entry.getKey();
                    int colIndex = entry.getValue();
                    Cell cell = row.getCell(colIndex);
                    Object value = getCellValue(cell);

                    if (value != null) {
                        Object converted = convertValue(value, field.getType());
                        field.setAccessible(true);
                        field.set(dto, converted);
                    }
                }

                resultList.add(dto);
            }
        }
        return resultList;
    }

    /**
     * ✅ Parse Excel into List<Map<String, Object>> using given columns and sheet name
     */
    public static List<Map<String, Object>> parseExcel(
            String filePath, List<String> columnNames, String sheetName) throws Exception {

        List<Map<String, Object>> resultList = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = (sheetName != null && workbook.getSheet(sheetName) != null)
                    ? workbook.getSheet(sheetName)
                    : workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.iterator();
            if (!rowIterator.hasNext()) return resultList;

            // Read header row
            Row headerRow = rowIterator.next();
            Map<String, Integer> headerIndexMap = getHeaderIndexMap(headerRow);

            // Validate all requested columns exist
            for (String col : columnNames) {
                if (!headerIndexMap.containsKey(col)) {
                    throw new IllegalArgumentException("Column '" + col + "' not found in Excel header");
                }
            }

            // Parse rows
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Map<String, Object> rowMap = new LinkedHashMap<>();

                for (String col : columnNames) {
                    int colIndex = headerIndexMap.get(col);
                    Cell cell = row.getCell(colIndex);
                    Object value = getCellValue(cell);
                    rowMap.put(col, value);
                }

                resultList.add(rowMap);
            }
        }

        return resultList;
    }

    // ---------------------- Helper Methods ----------------------

    private static Map<String, Integer> getHeaderIndexMap(Row headerRow) {
        Map<String, Integer> headerIndexMap = new HashMap<>();
        for (Cell cell : headerRow) {
            headerIndexMap.put(cell.getStringCellValue().trim(), cell.getColumnIndex());
        }
        return headerIndexMap;
    }

    private static <T> Map<Field, Integer> mapFieldsToColumns(Class<T> dtoClass, Map<String, Integer> headerIndexMap) {
        Map<Field, Integer> fieldColumnMap = new HashMap<>();
        for (Field field : dtoClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class)) {
                String headerName = field.getAnnotation(Column.class).value();
                Integer colIndex = headerIndexMap.get(headerName);
                if (colIndex != null) {
                    field.setAccessible(true);
                    fieldColumnMap.put(field, colIndex);
                }
            }
        }
        return fieldColumnMap;
    }

    private static Object getCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();

            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                } else {
                    return cell.getNumericCellValue();
                }

            case BOOLEAN:
                return cell.getBooleanCellValue();

            case FORMULA:
                return cell.getCellFormula();

            default:
                return null;
        }
    }


    private static Object convertValue(Object value, Class<?> targetType) {
        if (value == null) return null;
        if (targetType.isAssignableFrom(value.getClass())) return value;

        if (targetType == String.class) return String.valueOf(value);
        if (targetType == int.class || targetType == Integer.class)
            return (int) Double.parseDouble(value.toString());
        if (targetType == double.class || targetType == Double.class)
            return Double.parseDouble(value.toString());
        if (targetType == boolean.class || targetType == Boolean.class)
            return Boolean.parseBoolean(value.toString());
        if (targetType == Date.class && value instanceof Date)
            return value;

        return value.toString();
    }
}
