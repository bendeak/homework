package com.tests.ui.guru99;

import com.framework.core.BaseTest;
import com.tests.ui.guru99.pages.Guru99HomePage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("iFrame and Tab Handling")
class IFrameAndTabTest extends BaseTest {

    private static final String EXPECTED_NEW_TAB_TITLE = "Selenium Live Project for Practice";

    @Test
    @DisplayName("Clicking iframe ad image opens a new tab with the expected title")
    void iframeImageClickOpensNewTabWithExpectedTitle() {

        // Arrange — open the home page
        log.info("Step 1: Opening Guru99 home page");
        Guru99HomePage homePage = new Guru99HomePage(driver).open();
        assertTrue(homePage.isLoaded(), "Guru99 home page should be loaded");

        // Act — scroll to iframe, click image, capture pre-click handles
        log.info("Step 2: Clicking image inside iframe");
        Set<String> existingHandles = homePage.clickIframeImage();

        // Assert — new tab opens with expected title
        log.info("Step 3: Switching to new tab and verifying title");
        homePage.switchToNewTab(existingHandles);
        wait.forPage().titleContains("Selenium Live Project");
        String actualTitle = driver.getTitle();
        log.info("New tab title: '{}'", actualTitle);
        assertEquals(EXPECTED_NEW_TAB_TITLE, actualTitle,
                "New tab title should match expected");
    }
}
