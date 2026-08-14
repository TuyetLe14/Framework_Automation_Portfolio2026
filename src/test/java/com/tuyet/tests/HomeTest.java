package com.tuyet.tests;

import org.openqa.selenium.Dimension;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.tuyet.base.BaseTest;
import com.tuyet.constants.ConfigsData;
import com.tuyet.pages.HomePage;

public class HomeTest extends BaseTest {

    @Test(priority = 1, description = "Test 1: Kiểm tra thông tin cơ bản trên trang Home")
    public void testBasicInfo() {
        getDriver().get(ConfigsData.URL);
        HomePage homePage = new HomePage(getDriver());

        Assert.assertNotNull(homePage.getPageTitle(), "Title không được trống!");

        Assert.assertTrue(homePage.getNameText().toUpperCase().contains("LE HUYNH ANH TUYET"), "Lỗi: Tên hiển thị không đúng!");
        Assert.assertTrue(homePage.isPortraitVisible(), "Lỗi: Không load được ảnh chân dung!");
    }

    @Test(priority = 2, description = "Test 2: Kiểm tra logic chuyển đổi Theme (Dark/Light)")
    public void testThemeSwitching() {
        getDriver().get(ConfigsData.URL);
        HomePage homePage = new HomePage(getDriver());

        String beforeColor = homePage.getBgColor();

        homePage.toggleTheme();
        
        String afterColor = homePage.getBgColorAfterSwitch(beforeColor);
        System.out.println("Màu trước: " + beforeColor + " | Màu sau: " + afterColor);

        Assert.assertNotEquals(beforeColor.replaceAll("\\s+", ""), afterColor.replaceAll("\\s+", ""),
                "Lỗi: Màu nền không thay đổi sau khi Switch!");
    }

    @Test(priority = 3, description = "Kiểm tra nút Download CV")
    public void testCVDownloadLink() {
        getDriver().get(ConfigsData.URL);
        HomePage homePage = new HomePage(getDriver());

        String cvHref = homePage.getCVHref();
        System.out.println("Link CV: " + cvHref);

        Assert.assertTrue(cvHref.endsWith(".pdf"), "Lỗi: Link CV không phải file PDF!");
        Assert.assertEquals(homePage.verifyLinkStatus(cvHref), 200, "Lỗi: Link CV bị die (404)!");
    }

    @Test(priority = 4, description = "Test 4: Kiểm tra tính đúng đắn của các link mạng xã hội")
    public void testSocialLinks() {
        getDriver().get(ConfigsData.URL);
        HomePage homePage = new HomePage(getDriver());

        Assert.assertEquals(homePage.getGithubHref(), "https://github.com/TuyetLe14");
        Assert.assertTrue(homePage.getFacebookHref().contains("facebook.com/tuyet.lehuynhanh"));
        Assert.assertTrue(homePage.getTiktokHref().contains("tiktok.com"));
    }

    @Test(priority = 5, description = "5. Kiểm tra Responsive Layout (Mobile vs Desktop)")
    public void testResponsiveLayout() {
        getDriver().get(ConfigsData.URL);
        HomePage homePage = new HomePage(getDriver());
        getDriver().manage().window().setSize(new Dimension(390, 844));

        String layoutClass = homePage.getContentGridClass();
        System.out.println("Layout Class trên Mobile: " + layoutClass);

        Assert.assertTrue(layoutClass.contains("flex-col"), 
        "Lỗi: Mobile layout không chuyển sang dạng cột! Class tìm thấy: " + layoutClass);

        getDriver().manage().window().maximize();
    }
}