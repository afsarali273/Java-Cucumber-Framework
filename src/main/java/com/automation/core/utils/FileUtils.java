package com.automation.core.utils;

import com.automation.core.logging.LogManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtils {
    
    public static String readFile(String filePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            LogManager.error("Failed to read file: " + filePath, e);
            return null;
        }
    }
    
    public static void writeFile(String filePath, String content) {
        try {
            Files.write(Paths.get(filePath), content.getBytes());
            LogManager.info("File written successfully: " + filePath);
        } catch (IOException e) {
            LogManager.error("Failed to write file: " + filePath, e);
        }
    }
    
    public static boolean fileExists(String filePath) {
        return Files.exists(Paths.get(filePath));
    }
}
