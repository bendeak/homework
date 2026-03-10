package com.tests.ui.guru99.pages;

import com.framework.core.BasePage;
import org.openqa.selenium.*;

public class SeleniumTutorialPage extends BasePage {

    private static final By PAGE_HEADING = By.cssSelector("h1.entry-title");

    // The "wide red Join button" is not present currently due to possible changes
    // in the implementation.
    // The next best thing is the "Submit" button defined here:
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
        // ((JavascriptExecutor) driver).executeScript(
        // "document.body.dispatchEvent(new KeyboardEvent('keydown', {bubbles:
        // true}));");
        wait.on(SUBMIT_BUTTON).present();
        scrollToElement(SUBMIT_BUTTON);
        return wait.on(SUBMIT_BUTTON).isVisible();
    }
}
