package com.tuyet.base;

import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

public final class DriverManager {

    private static final ThreadLocal<WebDriver> DRIVER =
            new ThreadLocal<>();

    private DriverManager() {
        // Prevent object creation
    }

    public static void createDriver() {
        String browser = System.getProperty("browser", "chrome").toLowerCase();
        boolean headless = Boolean.parseBoolean(
                System.getProperty("headless", "false"));

        WebDriver driver;

        switch (browser) {
            case "firefox" -> {
                FirefoxOptions options = new FirefoxOptions();
                if (headless) {
                    options.addArguments("-headless");
                    options.addArguments("--width=1920", "--height=1080");
                }
                driver = new FirefoxDriver(options);
            }
            case "edge" -> {
                EdgeOptions options = new EdgeOptions();
                options.addArguments("--remote-allow-origins=*");
                if (headless) {
                    options.addArguments("--headless=new", "--no-sandbox",
                            "--disable-dev-shm-usage", "--window-size=1920,1080");
                } else {
                    options.addArguments("--start-maximized");
                }
                driver = new EdgeDriver(options);
            }
            case "chrome" -> {
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--remote-allow-origins=*");
                if (headless) {
                    options.addArguments("--headless=new", "--no-sandbox",
                            "--disable-dev-shm-usage", "--window-size=1920,1080");
                } else {
                    options.addArguments("--start-maximized");
                }
                driver = new ChromeDriver(options);
            }
            default -> throw new IllegalArgumentException(
                    "Unsupported browser: " + browser
                            + ". Use chrome, firefox or edge.");
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));

        DRIVER.set(driver);
    }

    public static WebDriver getDriver() {

        WebDriver driver = DRIVER.get();

        if (driver == null) {
            throw new IllegalStateException(
                    "WebDriver has not been initialized. "
                            + "Call DriverManager.createDriver() first."
            );
        }

        return driver;
    }

    public static void quitDriver() {

        WebDriver driver = DRIVER.get();

        if (driver != null) {

            try {
                driver.quit();

            } finally {
                DRIVER.remove();
            }
        }
    }
}
