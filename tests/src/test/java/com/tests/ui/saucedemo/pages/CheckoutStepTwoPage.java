package com.tests.ui.saucedemo.pages;

import com.framework.core.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CheckoutStepTwoPage extends BasePage {

    private static final By PAGE_TITLE = By.cssSelector(".title");
    private static final By SUMMARY_INFO = By.cssSelector(".checkout_summary_container");
    private static final By ORDER_ITEMS = By.cssSelector(".cart_item .inventory_item_name");
    private static final By FINISH_BUTTON = By.id("finish");

    public CheckoutStepTwoPage(WebDriver driver) {
        super(driver);
        assertTrue(isLoaded(), "CheckoutStepTwoPage failed to load: unexpected URL or missing elements");
    }

    public boolean isLoaded() {
        boolean correctUrl = driver.getCurrentUrl().contains("/checkout-step-two");
        boolean correctTitle = getText(PAGE_TITLE).equals("Checkout: Overview");
        boolean summaryVisible = wait.on(SUMMARY_INFO).isVisible();
        return correctUrl && correctTitle && summaryVisible;
    }

    public List<String> getOrderItemNames() {
        List<WebElement> items = wait.on(ORDER_ITEMS).allVisible();
        return items.stream().map(WebElement::getText).toList();
    }

    // Transition method to CheckoutCompletePage
    public CheckoutCompletePage finishOrder() {
        log.info("Finishing order");
        click(FINISH_BUTTON);
        return new CheckoutCompletePage(driver);
    }
}
