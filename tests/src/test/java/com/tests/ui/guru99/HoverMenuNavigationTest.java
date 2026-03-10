package com.tests.ui.guru99;

import com.framework.core.BaseTest;
import com.tests.ui.guru99.pages.Guru99HomePage;
import com.tests.ui.guru99.pages.SeleniumTutorialPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Hover Menu Navigation")
class HoverMenuNavigationTest extends BaseTest {

    @Test
    @DisplayName("Hovering Testing menu and clicking Selenium navigates to tutorial page with signup button")
    void seleniumLinkNavigatesToTutorialPageWithSignupButton() {

        // Arrange — open the home page
        log.info("Step 1: Opening Guru99 home page");
        Guru99HomePage homePage = new Guru99HomePage(driver).open();
        assertTrue(homePage.isLoaded(), "Guru99 home page should be loaded");

        // Act — hover Testing menu, click Selenium link
        log.info("Step 2: Navigating to Selenium tutorial via Testing menu");
        SeleniumTutorialPage seleniumPage = homePage.navigateToSeleniumViaMenu();
        assertTrue(seleniumPage.isLoaded(), "Selenium tutorial page should be loaded");

        // Assert — red signup button is displayed
        log.info("Step 3: Verifying red signup button is displayed");
        assertTrue(seleniumPage.isSubmitButtonDisplayed(),
                "Red signup Submit button should be visible near bottom of page");
    }
}
