package com.tuyet.tests;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.tuyet.base.BaseTest;
import com.tuyet.constants.ConfigsData;
import com.tuyet.pages.AboutPage;
import com.tuyet.pages.HomePage;

public class AboutTest extends BaseTest {
    private AboutPage aboutPage;

    @BeforeMethod
    public void setupPage() {
        aboutPage = new AboutPage(driver);
    }

    @Test(priority = 1, description = "Kiểm tra điều hướng đến trang About")
    public void testNavigationToAbout() {
        driver.get(ConfigsData.URL);

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.location.href='/#/about'");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("/#/about"));

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }

        System.out.println("🚩 URL hien tai khi dang dung hinh: " + driver.getCurrentUrl());
        Assert.assertTrue(driver.getCurrentUrl().contains("/#/about"));
    }

    @Test(priority = 2)
    public void testContactAndEducation() {
        driver.get(ConfigsData.URL + "/#/about");
        aboutPage.waitForPageLoaded();

        Assert.assertEquals(aboutPage.getEmail(), "lehuynhanh.tuyet10@gmail.com");
        Assert.assertEquals(aboutPage.getPhone(), "0817493884");
        Assert.assertEquals(aboutPage.getLocation(), "Ho Chi Minh, Vietnam");
        Assert.assertTrue(aboutPage.getUniversity().contains("UIT"), "Thiếu tên trường UIT!");
        Assert.assertTrue(aboutPage.getGPA().contains("8.13"), "GPA không khớp!");
    }

    @Test(priority = 3, description = "Kiểm tra thông tin học vấn và GPA")
    public void testEducationDetails() {
        driver.get(ConfigsData.URL + "/#/about");

        String uniText = aboutPage.getUniversity();
        Assert.assertTrue(uniText.contains("University of Information Technology"), "Tên trường đại học sai!");
        Assert.assertTrue(aboutPage.isGPADisplayed(), "GPA Badge không hiển thị!");
    }

    @Test(priority = 4, description = "Kiểm tra hình ảnh chân dung chính chủ")
    public void testVisualElements() {
        driver.get(ConfigsData.URL + "/#/about");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        wait.until(ExpectedConditions.urlContains("/#/about"));

        WebElement portrait = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("about-portrait")));

        ((JavascriptExecutor) driver).executeScript("arguments[0].style.border='5px solid red'", portrait);

        wait.until(driver -> (Boolean) ((JavascriptExecutor) driver).executeScript(
                "return arguments[0].complete && arguments[0].naturalWidth > 0", portrait));

        String actualSrc = portrait.getAttribute("src");
        System.out.println("DEBUG - Link ảnh thực tế: " + actualSrc);

        Assert.assertTrue(actualSrc.contains("about"),
                "Check Var FAIL: Link ảnh không chứa định danh 'about'. Link tìm thấy: " + actualSrc);

        int imageWidth = portrait.getSize().getWidth();
        Assert.assertTrue(imageWidth > 100, "Check Var FAIL: Ảnh quá nhỏ, có vẻ chưa load được hình thật!");
        Assert.assertTrue(aboutPage.isCrystalBackgroundPresent(), "Không tìm thấy Crystal Background!");
    }

    @Test(priority = 5, description = "Kiểm tra đồng bộ Theme (Dark/Light)")
    public void testThemeSync() {
        driver.get(ConfigsData.URL + "/#/about");

        String themeClasses = aboutPage.getThemeAttribute();
        System.out.println("Current Theme Classes: " + themeClasses);

        Assert.assertTrue(themeClasses.contains("transition-colors"), "Thiếu class transition của theme!");

        boolean isLight = aboutPage.isLightModeActive();
        System.out.println("Is Light Mode Active: " + isLight);
    }

    @Test(priority = 6, description = "Kiểm tra Responsive Layout (Stacked on Mobile)")
    public void testResponsiveLayout() {
        driver.get(ConfigsData.URL + "/#/about");
        driver.manage().window().setSize(new Dimension(390, 844));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        Assert.assertTrue(aboutPage.isStackedLayout(), "Layout không chuyển sang dạng stacked (1 cột) trên mobile!");
        driver.manage().window().maximize();
    }
}