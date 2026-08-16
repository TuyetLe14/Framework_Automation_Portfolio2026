package com.tuyet.base;

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

        ChromeOptions options = new ChromeOptions();

        options.addArguments("--remote-allow-origins=*");

        if ("true".equalsIgnoreCase(
                System.getProperty("headless", "false"))) {

            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");

        } else {

            options.addArguments("--start-maximized");
        }

        WebDriver driver = new ChromeDriver(options);

        driver.manage().timeouts().implicitlyWait(
                Duration.ofSeconds(10)
        );

        driver.manage().timeouts().pageLoadTimeout(
                Duration.ofSeconds(60)
        );

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
