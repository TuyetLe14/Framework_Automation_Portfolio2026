package com.tuyet.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AboutPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By emailValue = By.xpath("//p[text()='Email']/following-sibling::p");
    private By phoneText = By.xpath("//p[text()='Phone']/following-sibling::p");
    private By locationText = By.xpath("//p[text()='Location']/following-sibling::p");
    private By universityTitle = By.xpath("//h3[contains(text(), 'University of Information Technology')]");
    private By gpaValue = By.xpath("//span[contains(text(), 'GPA:')]");
    private By portraitImg = By.xpath("//img[@alt='Profile Photo']");

    private By mainContainer = By.xpath("//div[@id='root']/div[1]");
    // Container bao quanh grid để check layout
    private By contentGrid = By.xpath("//div[contains(@class, 'grid')]");
    private By crystalBackground = By.xpath("//canvas | //div[contains(@class, 'crystal-background')]");

    public AboutPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    private String getText(By locator) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        return el.getText().trim();
    }

    private String waitAndGetText(By locator) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", el);

        wait.until(d -> {
            String text = el.getText();
            return text != null && !text.trim().isEmpty();
        });

        ((JavascriptExecutor) driver).executeScript("arguments[0].style.border='2px solid #ec4899';", el);
        return el.getText().trim();
    }

    // --- CÁC HÀM GET DỮ LIỆU ---
    public String getEmail() {
        return getText(emailValue);
    }

    public String getPhone() {
        return getText(phoneText);
    }

    public String getLocation() {
        return getText(locationText);
    }

    public String getUniversity() {
        return getText(universityTitle);
    }

    public String getGPA() {
        return getText(gpaValue);
    }

    public boolean isGPADisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(gpaValue)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isPortraitVisible() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement img = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//img[@alt='Profile Photo']")));

            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", img);

            return (Boolean) ((JavascriptExecutor) driver).executeScript(
                    "return arguments[0].complete && arguments[0].naturalWidth > 0", img);
        } catch (Exception e) {
            return false;
        }
    }

    public String getBgColor() {
        WebElement container = wait.until(ExpectedConditions.presenceOfElementLocated(mainContainer));
        return container.getCssValue("background-color");
    }

    public boolean isStackedLayout() {
        WebElement grid = wait.until(ExpectedConditions.presenceOfElementLocated(contentGrid));
        String classes = grid.getAttribute("class");
        return classes.contains("grid-cols-1");
    }

    public String getThemeAttribute() {
        WebElement container = wait.until(ExpectedConditions.presenceOfElementLocated(mainContainer));
        return container.getAttribute("class");
    }

    public boolean isCrystalBackgroundPresent() {
        try {
            return driver.findElements(By.xpath("//div[contains(@class, 'fixed inset-0')]")).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public String getBackgroundStyle() {
        try {
            WebElement bg = wait.until(ExpectedConditions.presenceOfElementLocated(crystalBackground));
            return bg.getAttribute("style");
        } catch (Exception e) {
            return "Not found";
        }
    }

    public boolean isLightModeActive() {
        String currentClasses = getThemeAttribute();
        return currentClasses.contains("bg-white") || currentClasses.contains("text-slate-900");
    }

    public void waitForPageLoaded() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[text()='Email']")));
    }

    By portraitLocator = By.cssSelector(".portrait-image"); // Thay bằng locator thật của Tuyết nhé

    public String getPortraitSrc() {
        WebElement portrait = wait.until(ExpectedConditions.visibilityOfElementLocated(portraitLocator));
        return portrait.getAttribute("src");
    }
}