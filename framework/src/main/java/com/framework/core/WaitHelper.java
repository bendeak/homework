package com.framework.core;

import com.framework.config.FrameworkConfig;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

/**
 * Fluent wait helper.
 *
 * Usage:
 * waits.on(locator).visible()
 * waits.on(locator).clickable()
 * waits.on(locator).hasText("Submit")
 * waits.forPage().toLoad()
 * waits.forPage().urlContains("/dashboard")
 */
public class WaitHelper {

    private static final Logger log = LoggerFactory.getLogger(WaitHelper.class);

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebDriverWait shortWait;

    public WaitHelper(WebDriver driver) {
        this(driver, FrameworkConfig.explicitWaitSeconds());
    }

    public WaitHelper(WebDriver driver, int timeoutSeconds) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        this.shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
    }

    /** Entry point for element-level waits. */
    public ElementWait on(By locator) {
        return new ElementWait(locator, wait, shortWait);
    }

    /** Entry point for page-level waits. */
    public PageWait forPage() {
        return new PageWait(driver, wait);
    }

    /** Escape hatch — pass any custom ExpectedCondition directly. */
    public <T> T waitFor(ExpectedCondition<T> condition) {
        log.debug("Waiting for custom condition");
        return wait.until(condition);
    }

    // =========================================================================
    // Inner class: ElementWait
    // =========================================================================

    /**
     * Fluent API for waiting on a specific element.
     * Obtained via {@code waits.on(locator)}.
     */
    public static class ElementWait {

        private static final Logger log = LoggerFactory.getLogger(ElementWait.class);

        private final By locator;
        private final WebDriverWait wait;
        private final WebDriverWait shortWait;

        private ElementWait(By locator, WebDriverWait wait, WebDriverWait shortWait) {
            this.locator = locator;
            this.wait = wait;
            this.shortWait = shortWait;
        }

        /**
         * Waits until the element is present in the DOM and visible. Returns the
         * element.
         */
        public WebElement visible() {
            log.debug("Waiting for visible: {}", locator);
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        }

        /** Waits until all matching elements are visible. Returns the list. */
        public List<WebElement> allVisible() {
            log.debug("Waiting for all visible: {}", locator);
            return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
        }

        /**
         * Waits until the element is present in DOM (not necessarily visible). Returns
         * the element.
         */
        public WebElement present() {
            log.debug("Waiting for present: {}", locator);
            return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        }

        /** Waits until the element is visible and enabled. Returns the element. */
        public WebElement clickable() {
            log.debug("Waiting for clickable: {}", locator);
            return wait.until(ExpectedConditions.elementToBeClickable(locator));
        }

        /** Waits until the element is no longer visible or is removed from the DOM. */
        public void invisible() {
            log.debug("Waiting for invisible: {}", locator);
            wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
        }

        /** Waits until the element's visible text contains {@code text}. */
        public void hasText(String text) {
            log.debug("Waiting for text '{}' in: {}", text, locator);
            wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
        }

        /** Waits until the element's {@code attribute} equals {@code value}. */
        public void hasAttribute(String attribute, String value) {
            log.debug("Waiting for attribute '{}' = '{}' on: {}", attribute, value, locator);
            wait.until(ExpectedConditions.attributeToBe(locator, attribute, value));
        }

        /**
         * Soft check — returns true if element is visible within 3 seconds, false
         * otherwise.
         * Does not throw. Useful for conditional logic in page objects.
         */
        public boolean isVisible() {
            try {
                shortWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
                return true;
            } catch (TimeoutException | NoSuchElementException e) {
                return false;
            }
        }

        /**
         * Soft check — returns true if element is present in the DOM within 3 seconds.
         * Does not throw. Useful for conditional logic in page objects.
         */
        public boolean isPresent() {
            try {
                shortWait.until(ExpectedConditions.presenceOfElementLocated(locator));
                return true;
            } catch (TimeoutException | NoSuchElementException e) {
                return false;
            }
        }
    }

    // =========================================================================
    // Inner class: PageWait
    // =========================================================================

    /**
     * Fluent API for waiting on page-level conditions.
     * Obtained via {@code waits.forPage()}.
     */
    public static class PageWait {

        private static final Logger log = LoggerFactory.getLogger(PageWait.class);

        private final WebDriver driver;
        private final WebDriverWait wait;

        private PageWait(WebDriver driver, WebDriverWait wait) {
            this.driver = driver;
            this.wait = wait;
        }

        /** Waits until document.readyState == 'complete'. */
        public void toLoad() {
            log.debug("Waiting for page load");
            wait.until((ExpectedCondition<Boolean>) d -> "complete".equals(((JavascriptExecutor) d)
                    .executeScript("return document.readyState")));
        }

        /** Waits until the current URL contains {@code fragment}. */
        public void urlContains(String fragment) {
            log.debug("Waiting for URL to contain: {}", fragment);
            wait.until(ExpectedConditions.urlContains(fragment));
        }

        /** Waits until the page title contains {@code fragment}. */
        public void titleContains(String fragment) {
            log.debug("Waiting for title to contain: {}", fragment);
            wait.until(ExpectedConditions.titleContains(fragment));
        }

        /**
         * Waits until jQuery reports no active AJAX calls. Safe if jQuery is absent.
         */
        public void ajaxComplete() {
            log.debug("Waiting for AJAX to complete");
            wait.until((ExpectedCondition<Boolean>) d -> {
                try {
                    return Boolean.TRUE.equals(((JavascriptExecutor) d)
                            .executeScript("return jQuery.active == 0"));
                } catch (WebDriverException e) {
                    return true; // jQuery not present
                }
            });
        }

        /** Waits until a browser alert is present. Returns the Alert. */
        public Alert alert() {
            log.debug("Waiting for alert");
            return wait.until(ExpectedConditions.alertIsPresent());
        }
    }
}