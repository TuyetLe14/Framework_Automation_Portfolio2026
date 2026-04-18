package com.tuyet.tests;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.tuyet.base.BaseTest;
import com.tuyet.constants.ConfigsData;
import com.tuyet.pages.HomePage;

public class HomeTest extends BaseTest {

    @Test(priority = 1, description = "Test 1: Kiểm tra SEO và Nội dung Hero Section")
    public void testHeroSection() {
        driver.get(ConfigsData.URL);
        HomePage homePage = new HomePage(driver);

        String title = homePage.getPageTitle();
        System.out.println("Page Title: " + title);
        Assert.assertNotNull(title, "Title không được trống!");

        Assert.assertTrue(homePage.getNameText().toUpperCase().contains("ANH TUYET"), "Lỗi: Tên hiển thị không đúng!");
        Assert.assertTrue(homePage.isPortraitVisible(), "Lỗi: Không load được ảnh chân dung!");
    }

    @Test(priority = 2, description = "Test 2: Kiểm tra logic chuyển đổi Theme (Dark/Light)")
    public void testThemeSwitching() throws InterruptedException {
        driver.get(ConfigsData.URL);
        HomePage homePage = new HomePage(driver);

        String beforeColor = homePage.getBgColor();
        homePage.toggleTheme();

        Thread.sleep(1500);

        String afterColor = homePage.getBgColor();
        System.out.println("Màu trước: " + beforeColor + " | Màu sau: " + afterColor);

        String cleanBefore = beforeColor.replace(" ", "").replace(",1)", ")").replace("a(", "(");
        String cleanAfter = afterColor.replace(" ", "").replace(",1)", ")").replace("a(", "(");

        Assert.assertNotEquals(cleanBefore, cleanAfter, "Lỗi: Màu nền không thay đổi sau khi Switch!");
    }

    @Test(priority = 3, description = "Test 3: Kiểm tra định dạng các đường dẫn (Links Format)")
    public void testLinkUrlsFormat() {
        driver.get(ConfigsData.URL);
        HomePage homePage = new HomePage(driver);
        SoftAssert softAssert = new SoftAssert();
        String cvLink = homePage.getCVHref();
        softAssert.assertNotNull(cvLink, "Link CV bị null!");
        if (cvLink != null)
            softAssert.assertTrue(cvLink.endsWith(".pdf"), "Link CV phải trỏ tới file PDF!");

        softAssert.assertTrue(homePage.getGithubHref().contains("github.com"), "Link Github không hợp lệ!");
        softAssert.assertAll();
    }

    @Test
    public void testLinkResponseCodes() {
        try {
            driver.get(ConfigsData.URL);

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

            wait.until(ExpectedConditions.titleContains("Portfolio"));
            System.out.println("🚩 Đang ở URL thực tế: " + driver.getCurrentUrl());

            WebElement cvLink = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//a[contains(., 'Download') and contains(., 'CV')]")));

            String finalUrl = cvLink.getAttribute("href");
            System.out.println("🔗 Link tìm thấy: " + finalUrl);

            java.net.URL url = new java.net.URL(finalUrl);
            java.net.HttpURLConnection huc = (java.net.HttpURLConnection) url.openConnection();
            huc.setRequestMethod("GET");
            huc.connect();

            Assert.assertEquals(huc.getResponseCode(), 200, "❌ Lỗi 404: " + finalUrl);
            System.out.println("✅ CASE NÀY CHẮC CHẮN XANH RỒI!");

        } catch (Exception e) {
            Assert.fail("❌ Lỗi: " + e.getMessage());
        }
    }
}