package com.tests.ui.saucedemo.pages;

import com.framework.core.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private static final By USERNAME_INPUT = By.id("user-name");
    private static final By PASSWORD_INPUT = By.id("password");
    private static final By LOGIN_BUTTON = By.id("login-button");
    private static final By ERROR_MESSAGE = By.cssSelector("[data-test='error']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage open() {
        driver.get("https://www.saucedemo.com");
        wait.forPage().toLoad();
        return this;
    }

    public LoginPage openUrl(String url) {
        driver.get(url);
        wait.forPage().toLoad();
        return this;
    }

    // TODO: remove it if I go with transition method above
    public void submitLogin(String username, String password) {
        type(USERNAME_INPUT, username);
        type(PASSWORD_INPUT, password);
        click(LOGIN_BUTTON);
    }

    // Transition method to InventoryPage
    public InventoryPage loginAs(String username, String password) {
        type(USERNAME_INPUT, username);
        type(PASSWORD_INPUT, password);
        click(LOGIN_BUTTON);
        wait.forPage().toLoad();
        return new InventoryPage(driver);
    }

    // For negative test cases to trigger error message without transition to
    // InventoryPage
    public LoginPage clickLoginWithoutCredentials() {
        click(LOGIN_BUTTON);
        return this;
    }

    public boolean hasError() {
        return wait.on(ERROR_MESSAGE).isVisible();
    }

    public String getErrorMessage() {
        return getText(ERROR_MESSAGE);
    }
}
