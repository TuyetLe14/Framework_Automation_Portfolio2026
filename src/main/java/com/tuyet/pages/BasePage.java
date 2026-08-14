package com.tuyet.pages;

import com.tuyet.constants.ConfigsData;
import com.tuyet.utils.HighlightHelper;
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
        this.wait = new WebDriverWait(
                    driver,
                    Duration.ofSeconds(ConfigsData.TIMEOUT_EXPLICIT)
        );
        PageFactory.initElements(driver, this);
    }

    public void clickElement(By locator) {
        WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));
        scrollToElement(el);
        HighlightHelper.highlightElement(driver, el);
        try {
            el.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
        }
    }

    public String getTextElement(By locator) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        scrollToElement(el);
        HighlightHelper.highlightElement(driver, el);
        return el.getText().trim();
    }

    protected void sendKeys(By locator, String text) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        scrollToElement(el);
        HighlightHelper.highlightElement(driver, el);
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

    private void scrollToElement(WebElement element) {
       ((JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({block: 'center'});",
            element
       );
    }
}
