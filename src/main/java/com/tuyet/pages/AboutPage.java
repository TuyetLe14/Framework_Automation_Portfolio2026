package com.tuyet.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.util.Arrays;

public class AboutPage extends BasePage {

    private By emailValue = By.xpath("//p[text()='Email']/following-sibling::p");
    private By phoneText = By.xpath("//p[text()='Phone']/following-sibling::p");
    private By locationText = By.xpath("//p[text()='Location']/following-sibling::p");
    private By universityTitle = By.xpath("//h3[contains(text(), 'University of Information Technology')]");
    private By gpaValue = By.xpath("//span[contains(text(), 'GPA:')]");
    private By portraitImg = By.xpath("//img[@alt='Profile Photo']");
    private By mainContainer = By.xpath("//div[@id='root']/div[1]");
    private By contentGrid = By.xpath("//div[contains(@class, 'grid')]");
    private By crystalBackground = By.xpath("//canvas");
    private By qaBadge = By.xpath("//span[text()='QA Engineer']");
    private By profileSection = By.xpath("//h2[text()='Personal Profile']/parent::div/parent::div");
    private By degreeBadge = By.xpath("//span[contains(text(),'Degree:')]");
    private By majorText = By.xpath("//p[contains(text(),'Major:')]");
    private By focusAreaValue = By.xpath("//h4[text()='Focus Area']/following-sibling::p");
    private By backgroundValue = By.xpath("//h4[text()='Background']/following-sibling::p");
    private By ambitionValue = By.xpath("//h4[text()='Ambition']/following-sibling::p");
    private By homeNavLink = By.xpath("//nav//a[@href='#/']");
    
    public AboutPage(WebDriver driver) {
        super(driver);
    }

    public String getEmail() {
        return getTextElement(emailValue);
    }

    public String getPhone() {
        return getTextElement(phoneText);
    }

    public String getLocation() {
        return getTextElement(locationText);
    }

    public String getUniversity() {
        return getTextElement(universityTitle);
    }

    public String getGPA() {
        return getTextElement(gpaValue);
    }

    public String getPortraitSrc() {
        return getAttribute(portraitImg, "src");
    }

    public String getBackgroundStyle() {
        return getAttribute(crystalBackground, "style");
    }

    public String getThemeAttribute() {
        return getAttribute(mainContainer, "class");
    }

    public boolean isPortraitImageLoaded() {
        try {
            WebElement img = wait.until(ExpectedConditions.presenceOfElementLocated(portraitImg));
            return (Boolean) ((JavascriptExecutor) driver).executeScript(
                    "return arguments[0].complete && arguments[0].naturalWidth > 0", img);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isStackedLayout() {
        String columns = wait.until(
            ExpectedConditions.visibilityOfElementLocated(contentGrid))
            .getCssValue("grid-template-columns");
        return columns.trim().split("\\s+").length == 1;
    }

    public boolean isLightModeActive() {
        return Arrays.asList(getThemeAttribute().split("\\s+"))
            .contains("bg-slate-50");
    }

    public boolean isCrystalBackgroundPresent() {
        return isElementPresent(crystalBackground);
    }

    public void waitForPageLoaded() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailValue));
    }

    public void waitForLayoutChange() {
        wait.until(d -> isStackedLayout());
    }

    public boolean verifyUrlContains(String keyword) {
        try {
            return wait.until(ExpectedConditions.urlContains(keyword));
        } catch (Exception e) {
            return false;
        }
    }

    public String getQABadgeText() {
        return getTextElement(qaBadge);
    }

    public boolean isProfileVisibleAfterAnimation() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(profileSection)).isDisplayed();
    }

    public String getDegree() { return getTextElement(degreeBadge); }
    public String getMajor() { return getTextElement(majorText); }
    public String getFocusArea() { return getTextElement(focusAreaValue); }
    public String getBackground() { return getTextElement(backgroundValue); }
    public String getAmbition() { return getTextElement(ambitionValue); }
    public void goToHome() { clickElement(homeNavLink); }
}
