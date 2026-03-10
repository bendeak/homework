package com.framework.core;

import com.framework.config.FrameworkConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BasePage {

    protected final WebDriver driver;
    protected final WaitHelper wait;
    protected final Logger log = LoggerFactory.getLogger(getClass());

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WaitHelper(driver);
        PageFactory.initElements(driver, this);
    }

    // Navigation

    protected void navigateTo(String path) {
        String url = FrameworkConfig.baseUrl() + path;
        log.debug("Navigating to {}", url);
        driver.get(url);
    }

    // Common actions

    protected void click(By locator) {
        log.debug("Clicking: {}", locator);
        wait.on(locator).clickable().click();
    }

    protected void type(By locator, String text) {
        log.debug("Typing '{}' into: {}", text, locator);
        WebElement el = wait.on(locator).visible();
        el.clear();
        el.sendKeys(text);
    }

    protected void scrollToBottom() {
        log.debug("Scrolling to bottom of page");
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
    }

    protected void scrollToElement(By locator) {
        WebElement element = wait.on(locator).present();
        log.debug("Scrolling to element: {}", locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true)", element);
    }

    protected String getText(By locator) {
        return wait.on(locator).visible().getText();
    }

    public String getTitle() {
        return driver.getTitle();
    }
}
