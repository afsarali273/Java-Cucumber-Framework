package com.automation.core.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class EncryptionUtil {

    public static String encodeFile(String filePath) throws IOException {
        byte[] fileContent = Files.readAllBytes(Paths.get(filePath));
        return Base64.getEncoder().encodeToString(fileContent);
    }

    public static String decodeFile(String filePath) throws IOException {
        String encodedContent = new String(Files.readAllBytes(Paths.get(filePath)));
        byte[] decodedBytes = Base64.getDecoder().decode(encodedContent);
        return new String(decodedBytes);
    }

    public static String decodeString(String encodedString) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        return new String(decodedBytes);
    }

    public static void encryptFile(String inputFile, String outputFile) throws IOException {
        String encoded = encodeFile(inputFile);
        Files.write(Paths.get(outputFile), encoded.getBytes());
        System.out.println("✅ Encrypted: " + inputFile + " → " + outputFile);
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java EncryptionUtil <input-file> <output-file>");
            System.out.println("Example: java EncryptionUtil testData/credentials.json testData/credentials.json.encrypted");
            return;
        }
        try {
            encryptFile(args[0], args[1]);
        } catch (IOException e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }
}
