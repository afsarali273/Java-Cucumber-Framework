package com.automation.core.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogManager {
    private static final ThreadLocal<Logger> loggerThreadLocal = new ThreadLocal<>();

    public static Logger getLogger(Class<?> clazz) {
        Logger logger = loggerThreadLocal.get();
        if (logger == null) {
            logger = LoggerFactory.getLogger(clazz);
            loggerThreadLocal.set(logger);
        }
        return logger;
    }

    public static void info(String message) {
        getLogger(getCallerClass()).info(message);
    }

    public static void debug(String message) {
        getLogger(getCallerClass()).debug(message);
    }

    public static void warn(String message) {
        getLogger(getCallerClass()).warn(message);
    }

    public static void error(String message) {
        getLogger(getCallerClass()).error(message);
    }

    public static void error(String message, Throwable throwable) {
        getLogger(getCallerClass()).error(message, throwable);
    }

    private static Class<?> getCallerClass() {
        try {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            String className = stackTrace[3].getClassName();
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            return LogManager.class;
        }
    }

    public static void clearThreadLocal() {
        loggerThreadLocal.remove();
    }
}
