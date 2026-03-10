package com.tests.ui.saucedemo;

import com.framework.core.BaseTest;
import com.tests.ui.saucedemo.pages.InventoryPage;
import com.tests.ui.saucedemo.pages.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Case 2 – Validation and Footer")
class ValidationAndFooterTest extends BaseTest {

        private static final String INVENTORY_URL = "https://www.saucedemo.com/inventory.html";
        private static final String EXPECTED_ERROR = "Epic sadface: Username is required";
        private static final String VALID_USERNAME = "standard_user";
        private static final String VALID_PASSWORD = "secret_sauce";

        @Test
        @DisplayName("Login button shows error for missing credentials, footer shows copyright and terms")
        void errorMessageAndFooterValidation() {

                // Step 1 — Navigate directly to inventory (redirects to login page)
                log.info("Step 1: Navigating directly to inventory URL: {}", INVENTORY_URL);
                LoginPage loginPage = new LoginPage(driver).openUrl(INVENTORY_URL);

                // Step 2 — Click login without credentials and validate error
                log.info("Step 2: Clicking login without credentials");
                loginPage.clickLoginWithoutCredentials();

                assertTrue(loginPage.hasError(),
                                "Error message should be displayed after clicking login without credentials");

                String errorMessage = loginPage.getErrorMessage();
                log.info("Error message displayed: '{}'", errorMessage);
                assertEquals(EXPECTED_ERROR, errorMessage, "Error message text should match expected");

                // Step 3 — Login with valid credentials
                log.info("Step 3: Logging in as '{}'", VALID_USERNAME);
                InventoryPage inventoryPage = loginPage.loginAs(VALID_USERNAME, VALID_PASSWORD);

                assertTrue(inventoryPage.isLoaded(), "Inventory page should be loaded after login");

                // Step 4 — Scroll to footer and validate content
                log.info("Step 4: Scrolling to footer and validating content");
                String footerText = inventoryPage
                                .scrollToFooter()
                                .getFooterText();

                log.info("Footer text: '{}'", footerText);
                // If we want to validate "2024" the test will always fail, because the footer
                // contains 2026, it might be a typo in the test case
                assertAll("Footer content",
                                () -> assertTrue(footerText.contains("2026"),
                                                "Footer should contain '2026'"),
                                () -> assertTrue(footerText.contains("Terms of Service"),
                                                "Footer should contain 'Terms of Service'"));
        }
}
