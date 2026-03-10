package com.tests.ui.guru99.pages;

import com.framework.core.BasePage;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

import java.util.Set;

public class Guru99HomePage extends BasePage {

    private static final String URL = "http://demo.guru99.com/test/guru99home/";

    // Iframe locator — identified by id in the DOM
    private static final By BOTTOM_AD_IFRAME = By.id("a077aa5e");

    // Image inside the iframe — the only clickable image in the ad iframe
    private static final By BOTTOM_AD_IFRAME_IMAGE = By.cssSelector("img");

    // Top navigation — Testing menu item and its Selenium submenu link
    private static final By TESTING_HOVER_MENU = By.cssSelector("a.item[href*='software-testing']");
    private static final By SELENIUM_LINK = By.cssSelector("a.item[href*='selenium-tutorial']");

    public Guru99HomePage(WebDriver driver) {
        super(driver);
    }

    public Guru99HomePage open() {
        driver.get(URL);
        // Wait for a specific element rather than readyState because long load time due
        // to background script running
        wait.on(TESTING_HOVER_MENU).visible();
        return this;
    }

    public boolean isLoaded() {
        boolean correctUrl = driver.getCurrentUrl().contains("guru99home");
        boolean menuVisible = wait.on(TESTING_HOVER_MENU).isVisible();
        return correctUrl && menuVisible;
    }

    // Switches into the iframe, clicks the image inside it, then switches back to
    // the main document. Returns the handle of the main window so the caller can
    // switch back after inspecting the new tab.
    public Set<String> clickIframeImage() {
        Set<String> existingHandles = driver.getWindowHandles();
        log.info("Handles before click: {}", existingHandles);
        scrollToElement(BOTTOM_AD_IFRAME);
        enterFrame(BOTTOM_AD_IFRAME);
        log.info("Switched into iframe, clicking image");
        click(BOTTOM_AD_IFRAME_IMAGE);
        exitFrame();
        return existingHandles;
    }

    /**
     * Waits until a new tab is open and returns its handle.
     * Uses the main window handle to identify which handle is new.
     */
    public String switchToNewTab(Set<String> existingHandles) {
        // Wait until a handle appears that wasn't in the pre-click set
        wait.waitFor(d -> d.getWindowHandles().stream()
                .anyMatch(h -> !existingHandles.contains(h)));
        String newTab = driver.getWindowHandles().stream()
                .filter(h -> !existingHandles.contains(h))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No new tab found"));
        driver.switchTo().window(newTab);
        log.info("Switched to new tab: {}", newTab);
        return newTab;
    }

    public void closeTabAndSwitchTo(String targetHandle) {
        log.info("Closing current tab and switching back to: {}", targetHandle);
        driver.close();
        driver.switchTo().window(targetHandle);
    }

    public SeleniumTutorialPage navigateToSeleniumViaMenu() {
        log.info("Hovering over Testing menu to reveal dropdown");
        new Actions(driver)
                .moveToElement(wait.on(TESTING_HOVER_MENU).visible())
                .perform();
        log.info("Clicking Selenium link from dropdown");
        click(SELENIUM_LINK);
        wait.on(By.cssSelector("h1.entry-title")).visible();
        return new SeleniumTutorialPage(driver);
    }
}
