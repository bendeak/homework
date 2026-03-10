package com.tests.ui.saucedemo.pages;

import com.framework.core.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CheckoutStepOnePage extends BasePage {

    private static final By PAGE_TITLE = By.cssSelector(".title");
    private static final By FIRST_NAME_INPUT = By.id("first-name");
    private static final By LAST_NAME_INPUT = By.id("last-name");
    private static final By POSTAL_CODE_INPUT = By.id("postal-code");
    private static final By CONTINUE_BUTTON = By.id("continue");

    public CheckoutStepOnePage(WebDriver driver) {
        super(driver);
        assertTrue(isLoaded(), "CheckoutStepOnePage failed to load: unexpected URL or missing elements");
    }

    public boolean isLoaded() {
        boolean correctUrl = driver.getCurrentUrl().contains("/checkout-step-one");
        boolean correctTitle = getText(PAGE_TITLE).equals("Checkout: Your Information");
        boolean inputVisible = wait.on(FIRST_NAME_INPUT).isVisible();
        return correctUrl && correctTitle && inputVisible;
    }

    // Transition method to CheckoutStepTwoPage
    public CheckoutStepTwoPage fillDetailsAndContinue(String firstName, String lastName, String postalCode) {
        log.info("Filling checkout details: {} {} {}", firstName, lastName, postalCode);
        type(FIRST_NAME_INPUT, firstName);
        type(LAST_NAME_INPUT, lastName);
        type(POSTAL_CODE_INPUT, postalCode);
        click(CONTINUE_BUTTON);
        return new CheckoutStepTwoPage(driver);
    }
}
