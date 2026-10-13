package com.tuyet.tests;

import org.openqa.selenium.Dimension;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.time.Duration;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.tuyet.base.BaseTest;
import com.tuyet.constants.ConfigsData;
import com.tuyet.pages.AboutPage;
import com.tuyet.pages.HomePage;
import com.tuyet.constants.ProfileData;

public class AboutTest extends BaseTest {
    private AboutPage aboutPage;
    private HomePage homePage;

    @BeforeMethod
    public void setupPage() {
        aboutPage = new AboutPage(getDriver());
        homePage = new HomePage(getDriver());
    }

    private void openAboutPage() {
        getDriver().get(ConfigsData.URL + "/#/about");
        aboutPage.waitForPageLoaded();
    }
    
    @Test(description = "Kiểm tra điều hướng đến trang About")
    public void testNavigationToAbout() {
        getDriver().get(ConfigsData.URL);
        homePage.goToAboutPage();

        Assert.assertTrue(aboutPage.verifyUrlContains("/#/about"),
                "Lỗi: Không điều hướng đúng! URL hiện tại: " + getDriver().getCurrentUrl());
    }

    @Test(description = "Kiểm tra thông tin liên hệ và học vấn")
    public void testContactAndEducation() {
        openAboutPage();

        Assert.assertEquals(aboutPage.getEmail(), ProfileData.EMAIL);
        Assert.assertEquals(aboutPage.getPhone(), ProfileData.PHONE);
        Assert.assertEquals(aboutPage.getLocation(), ProfileData.LOCATION);
        Assert.assertTrue(aboutPage.getUniversity().contains("UIT"), "Thiếu tên trường UIT!");
        Assert.assertTrue(aboutPage.getGPA().contains(ProfileData.GPA), "GPA không khớp!");
    }

    @Test(description = "Kiểm tra hình ảnh chân dung và giao diện")
    public void testVisualElements() {
        openAboutPage();

        Assert.assertTrue(aboutPage.isPortraitImageLoaded(), "Lỗi: Ảnh chân dung không hiển thị đúng!");
        Assert.assertTrue(aboutPage.isCrystalBackgroundPresent(), "Không tìm thấy Crystal Background!");
    }

    @Test(description = "Kiểm tra Responsive Layout (Stacked on Mobile)")
    public void testResponsiveLayout() {
        openAboutPage();
        getDriver().manage().window().setSize(new Dimension(390, 844));
        aboutPage.waitForLayoutChange();
        Assert.assertTrue(aboutPage.isStackedLayout(), "Layout không chuyển sang dạng stacked (1 cột) trên mobile!");
        getDriver().manage().window().maximize();
    }

    @Test(description = "Kiểm tra định vị nghề nghiệp và Animation hiển thị")
    public void testProfessionalTagAndAnimation() {
        openAboutPage();
        Assert.assertEquals(aboutPage.getQABadgeText(), "QA ENGINEER", "Sai định vị nghề nghiệp!");
        Assert.assertTrue(aboutPage.isProfileVisibleAfterAnimation(), "Lỗi: Animation làm ẩn nội dung trang About!");
    }

    @Test(description = "Kiểm tra chi tiết học vấn và định hướng nghề nghiệp")
    public void testEducationAndCareerDetails() {
        openAboutPage();

        Assert.assertTrue(aboutPage.getDegree().contains(ProfileData.DEGREE), "Sai xếp loại bằng!");
        Assert.assertTrue(aboutPage.getMajor().contains(ProfileData.MAJOR), "Sai chuyên ngành!");
        Assert.assertTrue(aboutPage.getFocusArea().contains(ProfileData.FOCUS_AREA_KEYWORD), "Sai Focus Area!");
        Assert.assertTrue(aboutPage.getBackground().contains(ProfileData.BACKGROUND_KEYWORD), "Sai Background!");
        Assert.assertTrue(aboutPage.getAmbition().contains(ProfileData.AMBITION), "Sai Ambition!");
    }

    @Test(description = "Bấm Back của trình duyệt từ About quay về Home")
    public void testBrowserBackToHome() {
        getDriver().get(ConfigsData.URL);
        homePage.goToAboutPage();
        Assert.assertTrue(aboutPage.verifyUrlContains("/#/about"), "Không vào được About!");

        getDriver().navigate().back();

        new WebDriverWait(getDriver(), Duration.ofSeconds(10))
            .until(ExpectedConditions.not(ExpectedConditions.urlContains("about")));
        Assert.assertTrue(homePage.getNameText().toUpperCase().contains(ProfileData.FULL_NAME),
            "Lỗi: Back không quay về trang Home!");
    }

    @Test(description = "Bấm icon Home trên thanh điều hướng từ About")
    public void testHomeNavLinkFromAbout() {
        openAboutPage();

        aboutPage.goToHome();

        new WebDriverWait(getDriver(), Duration.ofSeconds(10))
            .until(ExpectedConditions.not(ExpectedConditions.urlContains("about")));
        Assert.assertTrue(homePage.getNameText().toUpperCase().contains(ProfileData.FULL_NAME),
            "Lỗi: Icon Home không đưa về trang chủ!");
    }
}
