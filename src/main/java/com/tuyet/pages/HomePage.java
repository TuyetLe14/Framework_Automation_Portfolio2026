package com.tuyet.pages;

import java.net.HttpURLConnection;
import java.net.URI;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage extends BasePage {

    private static final By HEADING_NAME = By.xpath("//h1");
    private static final By BUTTON_THEME_SWITCH = By.xpath("//span[text()='SWITCH']/preceding-sibling::div");
    private static final By CONTAINER_MAIN = By.xpath("//div[contains(@class, 'min-h-screen')]");
    private static final By IMG_PORTRAIT = By.xpath("//img[@alt='Portrait']");
    private static final By LINK_ABOUT = By.xpath("//a[contains(@href, 'about')]");
    private static final By CONTAINER_CONTENT_GRID = By.xpath("//div[contains(@class, 'flex-col') or contains(@class, 'md:flex-row')]");
    private static final By BUTTON_MOBILE_MENU = By.xpath("//button[contains(@class, 'md:hidden')]");
    private static final By LINK_CV = By.xpath("//a[contains(@href, '.pdf')]");
    private static final By LINK_GITHUB = By.xpath("//a[contains(@href, 'github.com')]");
    private static final By LINK_FACEBOOK = By.xpath("//a[contains(@href, 'facebook.com')]");
    private static final By LINK_TIKTOK = By.xpath("//a[contains(@href, 'tiktok.com')]");

    private static final int HTTP_TIMEOUT_MS = 5000;

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public String getNameText() {
        return getTextElement(HEADING_NAME).replace("\n", " ");
    }

    public String getBackgroundColor() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(CONTAINER_MAIN))
                .getCssValue("background-color");
    }

    public String getCVLink() {
        return getAttribute(LINK_CV, "href");
    }

    public String getGithubLink() {
        return getAttribute(LINK_GITHUB, "href");
    }

    public String getFacebookLink() {
        return getAttribute(LINK_FACEBOOK, "href");
    }

    public String getTiktokLink() {
        return getAttribute(LINK_TIKTOK, "href");
    }

    public String getContentGridClass() {
        return getAttribute(CONTAINER_CONTENT_GRID, "class");
    }

    public String getMainContainerClass() {
        return getAttribute(CONTAINER_MAIN, "class");
    }

    public HomePage toggleTheme() {
        clickElement(BUTTON_THEME_SWITCH);
        return this;
    }

    public HomePage openMobileMenu() {
        clickElement(BUTTON_MOBILE_MENU);
        return this;
    }

    public void navigateToAbout() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(HEADING_NAME));
        clickElement(LINK_ABOUT);
        wait.until(ExpectedConditions.urlContains("about"));
    }

    public boolean isPortraitDisplayed() {
        return isElementPresent(IMG_PORTRAIT);
    }

    public boolean isStackedLayout() {
        return getContentGridClass().contains("flex-col");
    }

    public boolean isThemeChanged(String previousColor) {
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(d -> !getBackgroundColor().replaceAll("\\s+", "")
                        .equalsIgnoreCase(previousColor.replaceAll("\\s+", "")));
        return true;
    }

    public int verifyLinkStatus(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) URI.create(url).toURL().openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(HTTP_TIMEOUT_MS);
            connection.connect();
            int statusCode = connection.getResponseCode();
            connection.disconnect();
            return statusCode;
        } catch (Exception e) {
            return 404;
        }
    }

    public String getBgColor() {
        return getBackgroundColor();
    }

    public String getBgColorAfterSwitch(String oldColor) {
        isThemeChanged(oldColor);
        return getBackgroundColor();
    }

    public String getCurrentThemeBackground() {
        return getBackgroundColor();
    }

    public String getCVHref() {
        return getCVLink();
    }

    public String getGithubHref() {
        return getGithubLink();
    }

    public String getFacebookHref() {
        return getFacebookLink();
    }

    public String getTiktokHref() {
        return getTiktokLink();
    }

    public void goToAboutPage() {
        navigateToAbout();
    }

    public boolean isPortraitVisible() {
        return isPortraitDisplayed();
    }
}