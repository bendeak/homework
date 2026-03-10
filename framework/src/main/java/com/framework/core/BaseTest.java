package com.framework.core;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseTest {

    protected WebDriver driver;
    protected WaitHelper wait;
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @BeforeEach
    public void setUp() {
        log.info("=== Starting test: {} ===", getClass().getSimpleName());
        driver = DriverFactory.getDriver();
        wait = new WaitHelper(driver);
    }

    @AfterEach
    public void tearDown() {
        log.info("=== Finished test: {} ===", getClass().getSimpleName());
        DriverFactory.quitDriver();
    }
}
