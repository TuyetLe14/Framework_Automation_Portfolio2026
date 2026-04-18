package com.tuyet.utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class HighlightHelper {
    public static void highlightElement(WebDriver driver, WebElement element) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript(
                    "arguments[0].style.border='3px solid red'; " +
                            "arguments[0].style.backgroundColor = 'yellow';",
                    element);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void markError(WebDriver driver, WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(
                "arguments[0].style.border='5px solid red'; " +
                        "arguments[0].style.backgroundColor='yellow'; " +
                        "arguments[0].style.outline='3px solid orange';",
                element);
    }
}