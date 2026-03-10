package com.tests.ui.saucedemo;

import com.framework.core.BaseTest;
import com.tests.ui.saucedemo.pages.InventoryPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Footer")
class FooterTest extends BaseTest {

        @Test
        @DisplayName("Footer displays current copyright year and Terms of Service text")
        void footerDisplaysCopyrightAndTerms() {

                // Step 1 — Login and navigate to inventory
                InventoryPage inventoryPage = new LoginHelper(driver).loginAs("standardUser");
                assertTrue(inventoryPage.isLoaded(), "Inventory page should be loaded after login");

                // Step 2 — Scroll to footer and validate content
                log.info("Scrolling to footer and validating content");
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
