package com.tests.ui.saucedemo.pages;

import com.framework.core.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class InventoryPage extends BasePage {

    private static final By PAGE_TITLE = By.cssSelector(".title");
    private static final By INVENTORY_LIST = By.cssSelector(".inventory_list");
    private static final By CART_BADGE = By.cssSelector(".shopping_cart_badge");
    private static final By CART_LINK = By.cssSelector(".shopping_cart_link");

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    // Kept it separate from constructor because of the transition method from
    // LoginPage
    public boolean isLoaded() {
        boolean correctUrl = driver.getCurrentUrl().contains("/inventory");
        boolean correctTitle = getText(PAGE_TITLE).equals("Products");
        boolean listVisible = wait.on(INVENTORY_LIST).isVisible();
        return correctUrl && correctTitle && listVisible;
    }

    public InventoryPage addToCart(String productName) {
        String buttonId = "add-to-cart-" + productName.toLowerCase().replace(" ", "-");
        click(By.id(buttonId));
        wait.forPage().toLoad();
        log.info("Added to cart: {}", productName);
        return this;
    }

    public int getCartCount() {
        if (!wait.on(CART_BADGE).isVisible())
            return 0;
        return Integer.parseInt(getText(CART_BADGE));
    }

    // Transition method to CartPage
    public CartPage goToCart() {
        click(CART_LINK);
        return new CartPage(driver);
    }
}
