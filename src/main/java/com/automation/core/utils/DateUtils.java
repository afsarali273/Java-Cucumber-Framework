package com.automation.core.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    
    public static String getCurrentDate(String format) {
        return new SimpleDateFormat(format).format(new Date());
    }
    
    public static String getCurrentTimestamp() {
        return new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    }
    
    public static long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }
}
