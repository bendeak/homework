package com.framework.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FrameworkConfig {

    private static final Logger log = LoggerFactory.getLogger(FrameworkConfig.class);
    private static final Properties props = new Properties();

    static {
        try (InputStream is = FrameworkConfig.class
                .getClassLoader()
                .getResourceAsStream("framework.properties")) {
            if (is != null) {
                props.load(is);
                log.info("Loaded framework.properties");
            } else {
                log.warn("framework.properties not found — using defaults / system properties only");
            }
        } catch (IOException e) {
            log.error("Failed to load framework.properties", e);
        }
    }

    private FrameworkConfig() {}

    /** Returns value from system property first, then properties file, then the provided default. */
    public static String get(String key, String defaultValue) {
        return System.getProperty(key, props.getProperty(key, defaultValue));
    }

    public static String get(String key) {
        return get(key, null);
    }


    public static String browser() {
        return get("browser", "chrome");
    }

    public static boolean headless() {
        return Boolean.parseBoolean(get("headless", "false"));
    }

    public static boolean chromeForTesting() {
        return Boolean.parseBoolean(get("chrome.for.testing", "true"));
    }

    public static String chromeBinary() {
        return get("chrome.binary", "");
    }

    public static String chromeVersion() {
        return get("chrome.version", "stable");
    }

    public static String firefoxVersion() {
        return get("firefox.version", "latest");
    }

    public static String edgeVersion() {
        return get("edge.version", "latest");
    }

    public static String baseUrl() {
        return get("base.url", "http://localhost");
    }

    public static String apiBaseUrl() {
        return get("api.base.url", "http://localhost/api");
    }

    public static int implicitWaitSeconds() {
        return Integer.parseInt(get("implicit.wait.seconds", "5"));
    }

    public static int explicitWaitSeconds() {
        return Integer.parseInt(get("explicit.wait.seconds", "15"));
    }
}
