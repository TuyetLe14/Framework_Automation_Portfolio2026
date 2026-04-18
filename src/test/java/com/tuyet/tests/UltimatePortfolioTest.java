package com.tuyet.tests;

import com.tuyet.base.BaseTest;
import com.tuyet.pages.HomePage;
import com.tuyet.pages.AboutPage;
import com.tuyet.constants.ConfigsData;
import org.openqa.selenium.Dimension;
import org.testng.Assert;
import org.testng.annotations.Test;

public class UltimatePortfolioTest extends BaseTest {

    @Test(description = "Kiểm tra đồng bộ Theme và màu sắc Gradient")
    public void testFullThemeAndGraphicsSync() {
        AboutPage aboutPage = new AboutPage(driver);

        String currentTheme = aboutPage.getThemeAttribute();

        if (currentTheme == null) {
            currentTheme = (String) ((org.openqa.selenium.JavascriptExecutor) driver)
                    .executeScript("return document.documentElement.getAttribute('data-theme');");
        }
        if ("light".equals(currentTheme)) {
            String bgStyle = aboutPage.getBackgroundStyle();
            Assert.assertTrue(bgStyle.contains("opacity"), "Lỗi: Theme light nhưng hiệu ứng không khớp!");
        }
        Assert.assertNotNull(currentTheme, "Lỗi: Không lấy được thuộc tính Theme (Null)");
    }

    // @Test(priority = 2, description = "Kiểm tra tính chính xác của dữ liệu học vấn UIT")
    // public void testDataAccuracy() {
    //     driver.get(ConfigsData.URL + "/about");
    //     AboutPage about = new AboutPage(driver);

    //     Assert.assertEquals(about.getEmail(), "lehuynhanh.tuyet10@gmail.com", "Data Integrity Error: Sai Email!");
    //     Assert.assertTrue(driver.getPageSource().contains("University of Information Technology"),
    //             "Thiếu tên trường UIT!");
    // }

    @Test(priority = 2, description = "Kiểm tra Responsive Layout cho Mobile")
    public void testResponsiveAboutPage() {
        driver.get(ConfigsData.URL + "/about");
        AboutPage about = new AboutPage(driver);

        driver.manage().window().setSize(new Dimension(390, 844));
        Assert.assertTrue(about.isStackedLayout(), "Lỗi: Layout không tự động chuyển thành cột đơn trên Mobile!");
        driver.manage().window().maximize();
    }
}