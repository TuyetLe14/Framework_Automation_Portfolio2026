package com.tuyet.tests;

import com.tuyet.base.BaseTest;
import com.tuyet.pages.HomePage;
import com.tuyet.pages.AboutPage;
import com.tuyet.constants.ConfigsData;
import org.openqa.selenium.Dimension;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ThemeSyncTest extends BaseTest {

    @Test(priority = 1, description = "Case 1: Kiểm tra đồng bộ Theme xuyên suốt (Theme Sync)")
    public void testThemeSyncBetweenPages() throws InterruptedException {
        driver.get(ConfigsData.URL);
        HomePage homePage = new HomePage(driver);

        homePage.toggleTheme();
        Thread.sleep(1500);
        String homeBg = homePage.getCurrentThemeBackground();

        homePage.goToAboutPage();

        AboutPage aboutPage = new AboutPage(driver);
        Thread.sleep(1000); 

        String aboutBg = aboutPage.getBgColor();
        System.out.println("Màu Home: " + homeBg + " | Màu About: " + aboutBg);

        Assert.assertEquals(aboutBg, homeBg, "Lỗi: Chuyển trang (bằng menu) là bị mất Theme!");
    }

    @Test(priority = 2, description = "Case 2: Kiểm tra dữ liệu cá nhân & Ảnh")
    public void testAboutDataIntegrity() {
        driver.get(ConfigsData.URL + "/about");
        AboutPage aboutPage = new AboutPage(driver);

        Assert.assertEquals(aboutPage.getEmail(), "lehuynhanh.tuyet10@gmail.com", "Email sai!");
        Assert.assertTrue(aboutPage.isPortraitVisible(), "Ảnh profile không hiện!");
    }

    @Test(priority = 3, description = "Case 3: Kiểm tra Responsive (Co giãn màn hình)")
    public void testResponsiveLayout() {
        driver.get(ConfigsData.URL + "/about");
        AboutPage aboutPage = new AboutPage(driver);

        driver.manage().window().setSize(new Dimension(390, 844));
        Assert.assertTrue(aboutPage.isStackedLayout(), "Lỗi: Mobile mà giao diện không xếp chồng!");

        driver.manage().window().maximize();
    }
}