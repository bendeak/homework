package com.framework.core;

import com.framework.config.FrameworkConfig;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class DriverFactory {

    private static final Logger log = LoggerFactory.getLogger(DriverFactory.class);
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    private DriverFactory() {
    }

    public static WebDriver getDriver() {
        if (driverThreadLocal.get() == null) {
            driverThreadLocal.set(createDriver());
        }
        return driverThreadLocal.get();
    }

    private static WebDriver createDriver() {
        String browser = FrameworkConfig.browser().toLowerCase();
        boolean headless = FrameworkConfig.headless();
        log.info("Creating {} driver (headless={})", browser, headless);

        WebDriver driver = switch (browser) {
            case "firefox" -> createFirefoxDriver(headless);
            case "edge" -> createEdgeDriver(headless);
            default -> createChromeDriver(headless);
        };

        // Page load strategy: EAGER waits only until the DOM is interactive, not until
        // all scripts/ads/analytics have finished. NORMAL (the default) blocks
        // driver.get()
        // until document.readyState == 'complete', which never happens on pages that
        // keep
        // background scripts running (e.g. ad-heavy pages), causing indefinite hangs.
        // Actual page readiness is verified by waiting on visible elements in each page
        // object.
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        driver.manage().window().maximize();
        return driver;
    }

    private static WebDriver createChromeDriver(boolean headless) {
        ChromeOptions opts = new ChromeOptions();
        opts.setPageLoadStrategy(PageLoadStrategy.EAGER);
        String version = FrameworkConfig.chromeVersion();
        String chromeBinary = FrameworkConfig.chromeBinary();

        if (FrameworkConfig.chromeForTesting()) {
            log.info("Using Chrome for Testing via Selenium Manager (version={})", version);
            opts.setBrowserVersion(version);
        } else {
            log.info("Using installed Chrome/Chromium, fetching ChromeDriver (version={})", version);
            WebDriverManager.chromedriver().browserVersion(version).setup();
        }

        if (chromeBinary != null && !chromeBinary.isEmpty()) {
            log.info("Using custom Chrome binary: {}", chromeBinary);
            opts.setBinary(chromeBinary);
        }

        if (headless) {
            opts.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage");
        }

        return new ChromeDriver(opts);
    }

    private static WebDriver createFirefoxDriver(boolean headless) {
        String version = FrameworkConfig.firefoxVersion();
        log.info("Using installed Firefox (GeckoDriver version={})", version);

        WebDriverManager.firefoxdriver().driverVersion(version).setup();

        FirefoxOptions opts = new FirefoxOptions();
        opts.setPageLoadStrategy(PageLoadStrategy.EAGER);
        if (headless)
            opts.addArguments("--headless");
        return new FirefoxDriver(opts);
    }

    private static WebDriver createEdgeDriver(boolean headless) {
        String version = FrameworkConfig.edgeVersion();
        log.info("Using installed Edge (EdgeDriver version={})", version);

        WebDriverManager.edgedriver().driverVersion(version).setup();

        EdgeOptions opts = new EdgeOptions();
        opts.setPageLoadStrategy(PageLoadStrategy.EAGER);
        if (headless)
            opts.addArguments("--headless");
        return new EdgeDriver(opts);
    }

    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            log.info("Quitting WebDriver");
            driver.quit();
            driverThreadLocal.remove();
        }
    }
}
