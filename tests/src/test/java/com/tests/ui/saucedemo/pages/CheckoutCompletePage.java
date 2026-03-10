package com.tests.ui.saucedemo.pages;

import com.framework.core.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CheckoutCompletePage extends BasePage {

    private static final By CONFIRMATION_HEADER = By.cssSelector(".complete-header");
    private static final By CONFIRMATION_TEXT = By.cssSelector(".complete-text");
    private static final By PONY_EXPRESS_IMAGE = By.cssSelector(".pony_express");

    public CheckoutCompletePage(WebDriver driver) {
        super(driver);
        wait.forPage().toLoad();
        assertTrue(isLoaded(), "CheckoutCompletePage failed to load: unexpected URL or missing elements");
    }

    public boolean isLoaded() {
        boolean correctUrl = driver.getCurrentUrl().contains("/checkout-complete");
        boolean headerVisible = wait.on(CONFIRMATION_HEADER).isVisible();
        boolean imageVisible = wait.on(PONY_EXPRESS_IMAGE).isVisible();
        return correctUrl && headerVisible && imageVisible;
    }

    public boolean isOrderConfirmed() {
        return wait.on(CONFIRMATION_HEADER).isVisible();
    }

    public String getConfirmationHeader() {
        return getText(CONFIRMATION_HEADER);
    }

    public String getConfirmationText() {
        return getText(CONFIRMATION_TEXT);
    }
}
