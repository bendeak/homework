package com.tests.ui.saucedemo.pages;

import com.framework.core.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CartPage extends BasePage {

    private static final By PAGE_TITLE = By.cssSelector(".title");
    private static final By CART_CONTAINER = By.cssSelector(".cart_contents_container");
    private static final By CART_ITEMS = By.cssSelector(".cart_item .inventory_item_name");
    private static final By CHECKOUT_BUTTON = By.id("checkout");

    public CartPage(WebDriver driver) {
        super(driver);
        wait.forPage().toLoad();
        assertTrue(isLoaded(), "CartPage failed to load: unexpected URL or missing elements");
    }

    public boolean isLoaded() {
        boolean correctUrl = driver.getCurrentUrl().contains("/cart");
        boolean correctTitle = getText(PAGE_TITLE).equals("Your Cart");
        boolean containerVisible = wait.on(CART_CONTAINER).isVisible();
        return correctUrl && correctTitle && containerVisible;
    }

    public List<String> getCartItemNames() {
        List<WebElement> items = wait.on(CART_ITEMS).allVisible();
        return items.stream().map(WebElement::getText).toList();
    }

    // Transition method to CheckoutStepOnePage
    public CheckoutStepOnePage proceedToCheckout() {
        click(CHECKOUT_BUTTON);
        return new CheckoutStepOnePage(driver);
    }
}
