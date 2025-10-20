package com.automation.core.interfaces;

/**
 * Interface for modular test lifecycle hooks.
 */
public interface ModularTestLifecycle {
    void setUp();
    void executeTest();
    void tearDown();
}

