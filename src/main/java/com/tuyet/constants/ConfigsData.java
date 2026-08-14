package com.tuyet.constants;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration Manager - Loads properties from application.properties
 */
public class ConfigsData {

    private static final Properties properties = new Properties();
    private static final String PROPERTIES_FILE = "application.properties";

    static {
        try (InputStream input = ConfigsData.class.getClassLoader()
                .getResourceAsStream(PROPERTIES_FILE)) {
            if (input == null) {
                System.out.println("⚠️ WARNING: " + PROPERTIES_FILE + " not found. Using default values.");
            } else {
                properties.load(input);
                System.out.println("✅ Successfully loaded " + PROPERTIES_FILE);
            }
        } catch (IOException e) {
            System.out.println("❌ ERROR: Failed to load " + PROPERTIES_FILE + ": " + e.getMessage());
        }
    }

    // ===== APPLICATION CONFIGURATION =====
    public static String URL = getProperty("app.url", "https://portfolio-2026.tgdd-ld9941.workers.dev/");
    public static String ENVIRONMENT = getProperty("app.environment", "dev");

    // ===== BROWSER CONFIGURATION =====
    public static String BROWSER_NAME = getProperty("browser.name", "chrome");
    public static boolean BROWSER_HEADLESS = getBooleanProperty("browser.headless", false);
    public static boolean BROWSER_MAXIMIZE_WINDOW = getBooleanProperty("browser.window.maximize", true);
    public static boolean BROWSER_ACCEPT_INSECURE_CERTS = getBooleanProperty("browser.accept.insecure.certs", true);

    // ===== TIMEOUT CONFIGURATION =====
    public static int TIMEOUT_IMPLICIT = getIntProperty("timeout.implicit", 10);
    public static int TIMEOUT_EXPLICIT = getIntProperty("timeout.explicit", 10);
    public static int TIMEOUT_PAGE_LOAD = getIntProperty("timeout.pageLoad", 60);
    public static int TIMEOUT_SCRIPT = getIntProperty("timeout.script", 30);

    // ===== SCREENSHOT CONFIGURATION =====
    public static boolean SCREENSHOT_ENABLED = getBooleanProperty("screenshot.enabled", true);
    public static boolean SCREENSHOT_ON_FAILURE = getBooleanProperty("screenshot.on.failure", true);
    public static String SCREENSHOT_PATH = getProperty("screenshot.path", "Test_Reports/Screenshots");

    // ===== VIDEO RECORDING CONFIGURATION =====
    public static boolean VIDEO_ENABLED = getBooleanProperty("video.enabled", true);
    public static boolean VIDEO_ON_FAILURE = getBooleanProperty("video.on.failure", true);
    public static boolean VIDEO_RECORD_ALL = getBooleanProperty("video.on.all.tests", false);
    public static String VIDEO_PATH = getProperty("video.path", "Test_Reports/Videos");

    // ===== LOGGING CONFIGURATION =====
    public static String LOG_LEVEL = getProperty("log.level", "INFO");

    // ===== REPORT CONFIGURATION =====
    public static boolean REPORT_ENABLED = getBooleanProperty("report.enabled", true);
    public static String REPORT_PATH = getProperty("report.path", "Test_Reports");
    public static String REPORT_TITLE = getProperty("report.title", "Test Automation Report");
    public static String REPORT_AUTHOR = getProperty("report.author", "QA Team");

    // ===== RETRY CONFIGURATION =====
    public static boolean RETRY_ENABLED = getBooleanProperty("retry.enabled", true);
    public static int RETRY_COUNT = getIntProperty("retry.count", 2);
    public static boolean RETRY_ONLY_FAILURES = getBooleanProperty("retry.test.failure.only", true);

    // ===== PARALLEL EXECUTION CONFIGURATION =====
    public static boolean PARALLEL_ENABLED = getBooleanProperty("parallel.enabled", false);
    public static int PARALLEL_THREADS = getIntProperty("parallel.threads", 3);
    public static String PARALLEL_METHOD = getProperty("parallel.method", "methods");

    /**
     * Get String property value
     * @param key Property key
     * @param defaultValue Default value if key not found
     * @return Property value or default
     */
    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * Get Integer property value
     * @param key Property key
     * @param defaultValue Default value if key not found
     * @return Property value as integer or default
     */
    public static int getIntProperty(String key, int defaultValue) {
        try {
            String value = properties.getProperty(key, String.valueOf(defaultValue));
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            System.out.println("⚠️ WARNING: Invalid integer value for property: " + key);
            return defaultValue;
        }
    }

    /**
     * Get Boolean property value
     * @param key Property key
     * @param defaultValue Default value if key not found
     * @return Property value as boolean or default
     */
    public static boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = properties.getProperty(key, String.valueOf(defaultValue));
        return Boolean.parseBoolean(value);
    }

    /**
     * Print all loaded configuration for debugging
     */
    public static void printConfiguration() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("TEST AUTOMATION FRAMEWORK - CONFIGURATION");
        System.out.println("=".repeat(60));
        System.out.println("APP URL: " + URL);
        System.out.println("ENVIRONMENT: " + ENVIRONMENT);
        System.out.println("BROWSER: " + BROWSER_NAME + " (Headless: " + BROWSER_HEADLESS + ")");
        System.out.println("TIMEOUT (Implicit): " + TIMEOUT_IMPLICIT + "s");
        System.out.println("TIMEOUT (Explicit): " + TIMEOUT_EXPLICIT + "s");
        System.out.println("SCREENSHOT ON FAILURE: " + SCREENSHOT_ON_FAILURE);
        System.out.println("VIDEO ON FAILURE: " + VIDEO_ON_FAILURE);
        System.out.println("REPORT PATH: " + REPORT_PATH);
        System.out.println("RETRY ENABLED: " + RETRY_ENABLED + " (Count: " + RETRY_COUNT + ")");
        System.out.println("=".repeat(60) + "\n");
    }
}