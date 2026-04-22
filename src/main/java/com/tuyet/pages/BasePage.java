package com.tuyet.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public void clickElement(By locator) {
        WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));
        scrollAndHighlight(el);
        try {
            el.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
        }
    }

    public String getTextElement(By locator) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        scrollAndHighlight(el);
        return el.getText().trim();
    }

    protected void sendKeys(By locator, String text) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        scrollAndHighlight(el);
        el.clear();
        el.sendKeys(text);
    }

    public String getAttribute(By locator, String attribute) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator)).getAttribute(attribute);
    }

    protected boolean isElementPresent(By locator) {
        try {
            return driver.findElements(locator).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    private void scrollAndHighlight(WebElement el) {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", el);
            ((JavascriptExecutor) driver).executeScript("arguments[0].style.border='3px solid #ec4899';", el);
        } catch (Exception e) {
        }
    }
}
