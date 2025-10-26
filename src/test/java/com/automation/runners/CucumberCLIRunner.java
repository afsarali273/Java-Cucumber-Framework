package com.automation.runners;

import io.cucumber.core.cli.Main;

import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public class CucumberCLIRunner {
    
    public static void main(String[] args) {
        String threadCount = getThreadCount();
        String myTag = "@UI";
        String tags = Objects.isNull(getTags())? myTag: getTags();
        
        String[] baseArgs = {
            "--glue", "com.automation.core.commonSteps",
            "--glue", "com.automation.stepdefinitions", 
            "--plugin", "pretty",
            "--plugin", "html:test-output/cucumber-reports/cucumber.html",
            "--plugin", "json:test-output/cucumber-reports/cucumber.json",
            "--plugin", "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm",
            "--threads", threadCount
        };
        
        String[] defaultArgs = tags != null ? 
            addTags(baseArgs, tags) : 
            addFeaturePath(baseArgs);
        
        String[] finalArgs = args.length > 0 ? args : defaultArgs;
        Main.main(finalArgs);
    }
    
    private static String getThreadCount() {
        String systemProperty = System.getProperty("thread_count");
        if (systemProperty != null) {
            return systemProperty;
        }
        
        try (InputStream input = CucumberCLIRunner.class.getClassLoader().getResourceAsStream("config.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            return prop.getProperty("thread.count", "1");
        } catch (Exception e) {
            return "1";
        }
    }
    
    private static String getTags() {
        return System.getProperty("cucumber.filter.tags");
    }
    
    private static String[] addTags(String[] baseArgs, String tags) {
        String[] result = new String[baseArgs.length + 3];
        System.arraycopy(baseArgs, 0, result, 0, baseArgs.length);
        result[baseArgs.length] = "--tags";
        result[baseArgs.length + 1] = tags;
        result[baseArgs.length + 2] = "src/test/resources/features";
        return result;
    }
    
    private static String[] addFeaturePath(String[] baseArgs) {
        String[] result = new String[baseArgs.length + 1];
        System.arraycopy(baseArgs, 0, result, 0, baseArgs.length);
        result[baseArgs.length] = "src/test/resources/features";
        return result;
    }
}