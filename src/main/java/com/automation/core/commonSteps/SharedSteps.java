package com.automation.core.commonSteps;

import com.automation.core.context.ScenarioContext;
import com.automation.core.logging.LogManager;
import io.cucumber.java.en.Then;

import java.util.Map;

/**
 * Shared step definitions used across API & UI step classes to avoid duplicates.
 */
public class SharedSteps {

    @Then("saved variable {string} should be {string}")
    public void savedVariableShouldBe(String variableName, String expected) {
        Object actualObj = ScenarioContext.get(variableName);
        String actual = actualObj == null ? null : actualObj.toString();
        String expectedResolved = replaceVariables(expected);
        if (actual == null && expectedResolved == null) return;
        if (actual == null || !actual.equals(expectedResolved)) {
            throw new AssertionError("Saved variable '" + variableName + "' expected to be '" + expectedResolved + "' but was '" + actual + "'");
        }
        LogManager.info("Verified saved variable '" + variableName + "' = " + actual);
    }

    @Then("saved variable {string} should not be null")
    public void savedVariableShouldNotBeNull(String variableName) {
        Object actual = ScenarioContext.get(variableName);
        if (actual == null) throw new AssertionError("Saved variable '" + variableName + "' should not be null");
        LogManager.info("Verified saved variable '" + variableName + "' is not null");
    }

    private String replaceVariables(String text) {
        String result = text;
        if (result == null) return null;
        for (Map.Entry<String, Object> entry : ScenarioContext.getAll().entrySet()) {
            String placeholder = "${" + entry.getKey() + "}";
            if (result.contains(placeholder)) {
                result = result.replace(placeholder, String.valueOf(entry.getValue()));
            }
        }
        return result;
    }
}

