package com.tuyet.pages;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By nameHeading = By.xpath("//h1//span");
    private By themeSwitch = By.xpath("//span[text()='SWITCH']/preceding-sibling::div");
    private By mainContainer = By.xpath("//div[contains(@class, 'min-h-screen')]");
    private By portraitImg = By.xpath("//img[@alt='Portrait']");
    private By aboutMenu = By.xpath("//button[.//span[text()='About']]");

    private By cvLink = By.xpath("//a[contains(@href, '.pdf')]");
    private By githubLink = By.xpath("//a[contains(@href, 'github.com')]");
    private By facebookLink = By.xpath("//a[contains(@href, 'facebook.com')]");
    private By tiktokLink = By.xpath("//a[contains(@href, 'tiktok.com')]");

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void goToAboutPage() {
        try {
            WebElement btn = wait.until(ExpectedConditions.presenceOfElementLocated(aboutMenu));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", btn);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);

            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("Lỗi điều hướng: " + e.getMessage());
            driver.findElement(aboutMenu).click();
        }
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public void toggleTheme() {
        wait.until(ExpectedConditions.elementToBeClickable(themeSwitch)).click();
    }

    public String getCurrentThemeBackground() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(mainContainer))
                .getCssValue("background-color");
    }

    // Giữ hàm cũ của Tuyết để không lỗi các test case cũ
    public String getBgColor() {
        return getCurrentThemeBackground();
    }

    // --- 3. Content Actions ---
    public String getNameText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(nameHeading))
                .getText().replace("\n", " ");
    }

    public boolean isPortraitVisible() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(portraitImg)).isDisplayed();
    }

    public String getCVHref() {
        return driver.findElement(cvLink).getAttribute("href");
    }

    public String getGithubHref() {
        return driver.findElement(githubLink).getAttribute("href");
    }

    public String getFacebookHref() {
        return driver.findElement(facebookLink).getAttribute("href");
    }

    public String getTiktokHref() {
        return driver.findElement(tiktokLink).getAttribute("href");
    }

    public int verifyLinkStatus(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.connect();
            return connection.getResponseCode();
        } catch (Exception e) {
            return 404;
        }
    }

    public void clickAboutMenu() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement aboutBtn = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//a[contains(@href, '/#/about')]")));

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", aboutBtn);
    }
}