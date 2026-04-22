package com.tuyet.tests;

import org.openqa.selenium.Dimension;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.tuyet.base.BaseTest;
import com.tuyet.constants.ConfigsData;
import com.tuyet.pages.AboutPage;
import com.tuyet.pages.HomePage;

public class AboutTest extends BaseTest {
    private AboutPage aboutPage;
    private HomePage homePage;

    @BeforeMethod
    public void setupPage() {
        aboutPage = new AboutPage(getDriver());
        homePage = new HomePage(getDriver());
    }

    @Test(priority = 1, description = "Kiểm tra điều hướng đến trang About")
    public void testNavigationToAbout() {
        getDriver().get(ConfigsData.URL);
        homePage.goToAboutPage();

        Assert.assertTrue(aboutPage.verifyUrlContains("/#/about"),
                "Lỗi: Không điều hướng đúng! URL hiện tại: " + getDriver().getCurrentUrl());
    }

    @Test(priority = 2, description = "Kiểm tra thông tin liên hệ và học vấn")
    public void testContactAndEducation() {
        getDriver().get(ConfigsData.URL + "/#/about");
        aboutPage.waitForPageLoaded();

        Assert.assertEquals(aboutPage.getEmail(), "lehuynhanh.tuyet10@gmail.com");
        Assert.assertEquals(aboutPage.getPhone(), "0817493884");
        Assert.assertEquals(aboutPage.getLocation(), "Ho Chi Minh, Vietnam");
        Assert.assertTrue(aboutPage.getUniversity().contains("UIT"), "Thiếu tên trường UIT!");
        Assert.assertTrue(aboutPage.getGPA().contains("8.13"), "GPA không khớp!");
    }

    @Test(priority = 3, description = "Kiểm tra hình ảnh chân dung và giao diện")
    public void testVisualElements() {
        getDriver().get(ConfigsData.URL + "/#/about");

        Assert.assertTrue(aboutPage.isPortraitImageLoaded(), "Lỗi: Ảnh chân dung không hiển thị đúng!");
        Assert.assertTrue(aboutPage.isCrystalBackgroundPresent(), "Không tìm thấy Crystal Background!");
    }

    @Test(priority = 4, description = "Kiểm tra đồng bộ Theme (Dark/Light)")
    public void testThemeSync() {
        getDriver().get(ConfigsData.URL + "/#/about");

        String themeClasses = aboutPage.getThemeAttribute();
        Assert.assertTrue(themeClasses.contains("transition-colors"), "Thiếu class transition của theme!");

        System.out.println("Is Light Mode Active: " + aboutPage.isLightModeActive());
    }

    @Test(priority = 5, description = "Kiểm tra Responsive Layout (Stacked on Mobile)")
    public void testResponsiveLayout() {
        getDriver().get(ConfigsData.URL + "/#/about");
        getDriver().manage().window().setSize(new Dimension(390, 844));
        aboutPage.waitForLayoutChange();
        Assert.assertTrue(aboutPage.isStackedLayout(), "Layout không chuyển sang dạng stacked (1 cột) trên mobile!");
        getDriver().manage().window().maximize();
    }

    @Test(priority = 6, description = "Kiểm tra định vị nghề nghiệp và Animation hiển thị")
    public void testProfessionalTagAndAnimation() {
        getDriver().get(ConfigsData.URL + "/#/about");
        Assert.assertEquals(aboutPage.getQABadgeText(), "QA ENGINEER", "Sai định vị nghề nghiệp!");
        Assert.assertTrue(aboutPage.isProfileVisibleAfterAnimation(), "Lỗi: Animation làm ẩn nội dung trang About!");
    }
}