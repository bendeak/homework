package com.tests.ui.saucedemo;

import com.framework.core.BaseTest;
import com.tests.ui.saucedemo.pages.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Login Validation")
class LoginValidationTest extends BaseTest {

    private static final String INVENTORY_URL = "https://www.saucedemo.com/inventory.html";
    private static final String REDIRECT_ERROR = "Epic sadface: You can only access '/inventory.html' when you are logged in.";
    private static final String EMPTY_SUBMIT_ERROR = "Epic sadface: Username is required";

    @Test
    @DisplayName("Unauthenticated access redirects to login and empty submission shows error message")
    void unauthenticatedAccessRedirectsAndEmptySubmitShowsError() {

        // Step 1 — Navigate directly to inventory — should redirect to login
        log.info("Step 1: Navigating directly to inventory URL: {}", INVENTORY_URL);
        LoginPage loginPage = new LoginPage(driver).openUrl(INVENTORY_URL);

        assertTrue(loginPage.isLoaded(), "Login page should be loaded after redirect");
        assertEquals(REDIRECT_ERROR, loginPage.getErrorMessage(),
                "Redirect error message should match expected");

        // Step 2 — Submit without credentials and validate the error
        log.info("Step 2: Clicking login without credentials");
        loginPage.clickLoginWithoutCredentials();

        assertEquals(EMPTY_SUBMIT_ERROR, loginPage.getErrorMessage(),
                "Empty submit error message should match expected");
    }
}
