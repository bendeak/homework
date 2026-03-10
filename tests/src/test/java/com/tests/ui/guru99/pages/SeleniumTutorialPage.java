package com.tests.ui.guru99.pages;

import com.framework.core.BasePage;
import org.openqa.selenium.*;

public class SeleniumTutorialPage extends BasePage {

    private static final By PAGE_HEADING = By.cssSelector("h1.entry-title");

    // NOTE: The spec referenced a "wide red Join Now button" which no longer
    // exists on this page. Asserting the red Submit button in the
    // "Learn Selenium in 7 Days!" signup section as the closest equivalent.
    private static final By SUBMIT_BUTTON = By.cssSelector("button.cb-form-group__btn");

    public SeleniumTutorialPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        boolean correctUrl = driver.getCurrentUrl().contains("selenium-tutorial");
        boolean headingVisible = wait.on(PAGE_HEADING).isVisible();
        return correctUrl && headingVisible;
    }

    // Specific order is needed because the ConvertBox that contains the submit
    // button seems to use triggered-injection, so we need to interact with the page
    // first to trigger the injection, then wait for the button to be present before
    // trying to scroll to it.
    public boolean isSubmitButtonDisplayed() {
        wait.on(PAGE_HEADING).visible().click();
        wait.on(SUBMIT_BUTTON).present();
        scrollToElement(SUBMIT_BUTTON);
        return wait.on(SUBMIT_BUTTON).isVisible();
    }
}
