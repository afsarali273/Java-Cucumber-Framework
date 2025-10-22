package com.automation.core.logging;

/**
 * Utility for colorful console logging using ANSI color codes.
 */
public class ColoredLogger {
    
    // ANSI Color Codes
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";
    
    // Bright Colors
    public static final String BRIGHT_RED = "\u001B[91m";
    public static final String BRIGHT_GREEN = "\u001B[92m";
    public static final String BRIGHT_YELLOW = "\u001B[93m";
    public static final String BRIGHT_BLUE = "\u001B[94m";
    public static final String BRIGHT_PURPLE = "\u001B[95m";
    public static final String BRIGHT_CYAN = "\u001B[96m";
    
    // Background Colors
    public static final String BG_RED = "\u001B[41m";
    public static final String BG_GREEN = "\u001B[42m";
    public static final String BG_YELLOW = "\u001B[43m";
    
    // Styles
    public static final String BOLD = "\u001B[1m";
    public static final String UNDERLINE = "\u001B[4m";
    
    /**
     * Print info message in blue.
     */
    public static void info(String message) {
        System.out.println(BRIGHT_BLUE + "[INFO] " + message + RESET);
    }
    
    /**
     * Print success/pass message in green.
     */
    public static void pass(String message) {
        System.out.println(BRIGHT_GREEN + "✓ [PASS] " + message + RESET);
    }
    
    /**
     * Print failure message in red.
     */
    public static void fail(String message) {
        System.out.println(BRIGHT_RED + "✗ [FAIL] " + message + RESET);
    }
    
    /**
     * Print warning message in yellow.
     */
    public static void warn(String message) {
        System.out.println(BRIGHT_YELLOW + "⚠ [WARN] " + message + RESET);
    }
    
    /**
     * Print action message in cyan.
     */
    public static void action(String message) {
        System.out.println(BRIGHT_CYAN + "➤ [ACTION] " + message + RESET);
    }
    
    /**
     * Print debug message in purple.
     */
    public static void debug(String message) {
        System.out.println(BRIGHT_PURPLE + "[DEBUG] " + message + RESET);
    }
    
    /**
     * Print header/banner message.
     */
    public static void header(String message) {
        System.out.println(BOLD + BRIGHT_CYAN + "\n" + "=".repeat(60) + RESET);
        System.out.println(BOLD + BRIGHT_CYAN + message + RESET);
        System.out.println(BOLD + BRIGHT_CYAN + "=".repeat(60) + "\n" + RESET);
    }
    
    /**
     * Print error with background.
     */
    public static void error(String message) {
        System.out.println(BG_RED + WHITE + BOLD + " ERROR " + RESET + " " + BRIGHT_RED + message + RESET);
    }
}
