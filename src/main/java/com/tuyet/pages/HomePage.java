package com.tuyet.pages;

import java.net.HttpURLConnection;
import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage extends BasePage {

    private By nameHeading = By.xpath("//h1");
    private By themeSwitch = By.xpath("//span[text()='SWITCH']/preceding-sibling::div");
    private By mainContainer = By.xpath("//div[contains(@class, 'min-h-screen')]");
    private By portraitImg = By.xpath("//img[@alt='Portrait']");
    private By aboutMenu = By.xpath("//a[contains(@href, 'about')]");
    private By contentGrid = By.xpath("//div[contains(@class, 'flex-col') or contains(@class, 'md:flex-row')]");
    private By mobileMenuButton = By.xpath("//button[contains(@class, 'md:hidden')]");

    private By cvLink = By.xpath("//a[contains(@href, '.pdf')]");
    private By githubLink = By.xpath("//a[contains(@href, 'github.com')]");
    private By facebookLink = By.xpath("//a[contains(@href, 'facebook.com')]");
    private By tiktokLink = By.xpath("//a[contains(@href, 'tiktok.com')]");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public HomePage toggleTheme() {
        clickElement(themeSwitch);
        return this;
    }

    public void goToAboutPage() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(nameHeading));
        clickElement(aboutMenu);
        wait.until(ExpectedConditions.urlContains("about"));
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public String getCurrentThemeBackground() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(mainContainer))
                .getCssValue("background-color");
    }

    public String getBgColor() {
        return getCurrentThemeBackground();
    }

    public String getBgColorAfterSwitch(String oldColor) {
        wait.until(d -> !getCurrentThemeBackground().equalsIgnoreCase(oldColor));
        return getCurrentThemeBackground();
    }

    public String getNameText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(nameHeading))
                .getText().replace("\n", " ");
    }

    public boolean isPortraitVisible() {
        return isElementPresent(portraitImg);
    }

    public int verifyLinkStatus(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.connect();
            int code = connection.getResponseCode();
            connection.disconnect();
            return code;
        } catch (Exception e) {
            return 404;
        }
    }

    public String getCVHref() {
        return getAttribute(cvLink, "href");
    }

    public String getGithubHref() {
        return getAttribute(githubLink, "href");
    }

    public String getFacebookHref() {
        return getAttribute(facebookLink, "href");
    }

    public String getTiktokHref() {
        return getAttribute(tiktokLink, "href");
    }

    public String getContentGridClass() {
        return getAttribute(contentGrid, "class");
    }

    public String getMainContainerClass() {
        return getAttribute(mainContainer, "class");
    }

    public String getThemeClass() {
        return getAttribute(mainContainer, "class");
    }

    public boolean isStackedLayout() {
        return getAttribute(mainContainer, "class").contains("flex-col");
    }

    public HomePage openMobileMenu() {
        wait.until(ExpectedConditions.elementToBeClickable(mobileMenuButton)).click();
        return this;
    }
}