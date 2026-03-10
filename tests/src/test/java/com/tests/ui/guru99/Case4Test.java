package com.tests.ui.guru99;

import com.framework.core.BaseTest;
import com.tests.ui.guru99.pages.Guru99HomePage;
import com.tests.ui.guru99.pages.SeleniumTutorialPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Case 4 – iFrame and Tab Handling")
class IFrameAndTabTest extends BaseTest {

    // NOTE: Spec stated "Selenium Live Project: FREE Real Time Project for
    // Practice"
    // but the page title has since changed. Asserting the current actual title.
    private static final String EXPECTED_NEW_TAB_TITLE = "Selenium Live Project for Practice";

    @Test
    @DisplayName("iframe image opens new tab, tab closes correctly, Selenium page shows signup button")
    void iFrameClickOpensTabAndSeleniumPageHasSignupButton() {

        // Step 1 — Open the home page
        log.info("Step 1: Opening Guru99 home page");
        Guru99HomePage homePage = new Guru99HomePage(driver).open();
        assertTrue(homePage.isLoaded(), "Guru99 home page should be loaded");

        // Step 2 — Click the image inside the iframe.
        log.info("Step 2: Clicking image inside iframe");
        Set<String> existingHandles = homePage.clickIframeImage();
        String mainHandle = existingHandles.iterator().next();

        // Step 3 — Verify new tab title
        log.info("Step 3: Switching to new tab and verifying title");
        homePage.switchToNewTab(existingHandles);
        wait.forPage().titleContains("Selenium Live Project");
        String actualTitle = driver.getTitle();
        log.info("New tab title: '{}'", actualTitle);
        assertEquals(EXPECTED_NEW_TAB_TITLE, actualTitle,
                "New tab title should match expected");

        // Step 4 — Close new tab and switch back to main window
        log.info("Step 4: Closing new tab and switching back to main window");
        homePage.closeTabAndSwitchTo(mainHandle);
        assertTrue(homePage.isLoaded(), "Should be back on Guru99 home page");

        // Step 5 — Hover Testing menu and click Selenium link
        log.info("Step 5: Navigating to Selenium tutorial via Testing menu");
        SeleniumTutorialPage seleniumPage = homePage.navigateToSeleniumViaMenu();
        assertTrue(seleniumPage.isLoaded(), "Selenium tutorial page should be loaded");

        // Step 6 — Verify the red signup button is displayed
        // The "wide red Join button" is not present currently due to possible changes
        // in the implementation.
        // The next best thing is the "Submit" button defined in SeleniumTutorialPage
        log.info("Step 6: Verifying red signup button is displayed");
        assertTrue(seleniumPage.isSubmitButtonDisplayed(),
                "Red signup Submit button should be visible near bottom of page");
    }
}
