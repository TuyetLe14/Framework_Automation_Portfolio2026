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
    public void testThemeSyncBetweenPages() {
        org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(getDriver(), java.time.Duration.ofSeconds(10));
        getDriver().get(ConfigsData.URL);
        HomePage homePage = new HomePage(getDriver());

        String oldColor = homePage.getBgColor();
        homePage.toggleTheme();
        homePage.getBgColorAfterSwitch(oldColor);

        homePage.goToAboutPage();

        AboutPage aboutPage = new AboutPage(getDriver());
        wait.until(d -> aboutPage.isLightModeActive());

        Assert.assertTrue(aboutPage.isLightModeActive(), 
        "Lỗi: Sang trang About nhưng không thấy Light Mode active!");
    }

    @Test(priority = 2, description = "Case 2: Kiểm tra giữ Theme sau khi F5 (Reload) trang")
    public void testThemePersistenceOnReload() {
        getDriver().get(ConfigsData.URL);
        HomePage homePage = new HomePage(getDriver());

        String oldColor = homePage.getBgColor();
        homePage.toggleTheme();
        homePage.getBgColorAfterSwitch(oldColor);

        homePage.goToAboutPage();
        AboutPage aboutPage = new AboutPage(getDriver());
        aboutPage.waitForPageLoaded();

        boolean isLightBefore = aboutPage.isLightModeActive();
        System.out.println("Trước khi Reload - Is Light Mode: " + isLightBefore);

        getDriver().navigate().refresh();
        aboutPage.waitForPageLoaded();

        boolean isLightAfter = aboutPage.isLightModeActive();
        System.out.println("Sau khi Reload - Is Light Mode: " + isLightAfter);

        Assert.assertEquals(aboutPage.isLightModeActive(), isLightBefore,
                "Lỗi: Reload trang là bị reset Theme (Dữ liệu chưa được lưu vào LocalStorage)");
    }

    @Test(priority = 3, description = "Case 3: Kiểm tra tính toàn vẹn dữ liệu trang About")
    public void testAboutDataIntegrity() {
        getDriver().get(ConfigsData.URL + "/#/about");
        AboutPage aboutPage = new AboutPage(getDriver());
        aboutPage.waitForPageLoaded();

        Assert.assertEquals(aboutPage.getEmail(), "lehuynhanh.tuyet10@gmail.com", "Email sai!");
        Assert.assertEquals(aboutPage.getPhone(), "0817493884", "Số điện thoại sai!");
        Assert.assertTrue(aboutPage.isPortraitImageLoaded(), "Ảnh profile không hiện!");
    }

    @Test(priority = 4, description = "Case 4: Kiểm tra Responsive Layout trên Mobile")
    public void testResponsiveLayout() {
        getDriver().get(ConfigsData.URL + "/#/about");
        AboutPage aboutPage = new AboutPage(getDriver());

        getDriver().manage().window().setSize(new Dimension(390, 844));
        aboutPage.waitForLayoutChange();
        Assert.assertTrue(aboutPage.isStackedLayout(), "Lỗi: Mobile mà giao diện không xếp chồng!");

        getDriver().manage().window().maximize();
    }
}