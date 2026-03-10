package com.tests.ui.saucedemo;

import com.framework.core.BaseTest;
import com.tests.ui.saucedemo.pages.InventoryPage;
import com.tests.ui.saucedemo.pages.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// This is a combined test for login validation and footer content as per the
// assignment spec.
// I kept them separate in the codebase (LoginValidationTest and FooterTest) to
// follow best practices, but this test serves as the required "Case 2" that
// covers both concerns together.

@DisplayName("Case 2 – Login Validation and Footer Content")
class Case2Test extends BaseTest {

        private static final String INVENTORY_URL = "https://www.saucedemo.com/inventory.html";
        private static final String EXPECTED_ERROR = "Epic sadface: Username is required";

        @Test
        @DisplayName("Guest is redirected to login, empty submit shows error, footer shows copyright and terms after sign in")
        void loginValidationAndFooterContentAreCorrect() {

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

                log.info("Step 3: Logging in as standardUser");
                InventoryPage inventoryPage = new LoginHelper(driver).loginAs("standardUser");
                assertTrue(inventoryPage.isLoaded(), "Inventory page should be loaded after login");

                assertTrue(inventoryPage.isLoaded(), "Inventory page should be loaded after login");

                // Step 4 — Scroll to footer and validate content
                log.info("Step 4: Scrolling to footer and validating content");
                String footerText = inventoryPage
                                .scrollToFooter()
                                .getFooterText();

                log.info("Footer text: '{}'", footerText);
                assertAll("Footer content",
                                () -> assertTrue(footerText.contains("2026"),
                                                "Footer should contain '2026'"),
                                () -> assertTrue(footerText.contains("Terms of Service"),
                                                "Footer should contain 'Terms of Service'"));
        }
}
