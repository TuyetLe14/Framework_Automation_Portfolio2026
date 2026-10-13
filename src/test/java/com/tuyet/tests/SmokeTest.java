package com.tuyet.tests;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.tuyet.base.BaseTest;
import com.tuyet.constants.ConfigsData;

public class SmokeTest extends BaseTest {

    @DataProvider(name = "routes")
    public Object[][] routes() {
        return new Object[][] {
                { "/" },
                { "/#/about" }
        };
    }

    private WebDriver openRoute(String route) {
        WebDriver driver = getDriver();
        driver.get(ConfigsData.URL + route);
        waitForContent(driver);
        return driver;
    }

    private void waitForContent(WebDriver driver) {
        new WebDriverWait(driver, Duration.ofSeconds(15)).until(d -> {
            JavascriptExecutor js = (JavascriptExecutor) d;
            Boolean ready = "complete".equals(js.executeScript("return document.readyState"));
            Long textLength = (Long) js.executeScript("return document.body.innerText.trim().length");
            return ready && textLength != null && textLength > 0;
        });
    }

    @Test(description = "Tiêu đề tab của trang đúng")
    public void testPageTitle() {
        WebDriver driver = openRoute("/");
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.titleContains("Snow"));
        Assert.assertTrue(driver.getTitle().contains("Snow"),
                "Lỗi: Tiêu đề tab không đúng! Hiện tại: " + driver.getTitle());
    }

    @Test(dataProvider = "routes", description = "Tất cả ảnh trên trang đều tải được")
    @SuppressWarnings("unchecked")
    public void testNoBrokenImages(String route) {
        WebDriver driver = openRoute(route);
        List<String> broken = (List<String>) ((JavascriptExecutor) driver).executeScript(
                "return Array.from(document.images)"
                        + ".filter(i => !i.complete || i.naturalWidth === 0)"
                        + ".map(i => i.src);");
        Assert.assertTrue(broken.isEmpty(), "Lỗi: Có ảnh bị lỗi ở " + route + ": " + broken);
    }

    @Test(dataProvider = "routes", description = "Không có thẻ link nào bị thiếu href")
    @SuppressWarnings("unchecked")
    public void testLinksHaveHref(String route) {
        WebDriver driver = openRoute(route);
        List<String> emptyLinks = (List<String>) ((JavascriptExecutor) driver).executeScript(
                "return Array.from(document.querySelectorAll('a'))"
                        + ".filter(a => !(a.getAttribute('href') || '').trim())"
                        + ".map(a => (a.innerText || a.outerHTML).slice(0, 60));");
        Assert.assertTrue(emptyLinks.isEmpty(),
                "Lỗi: Có link thiếu href ở " + route + ": " + emptyLinks);
    }

    @Test(description = "Refresh (F5) ở trang About vẫn giữ nguyên trang")
    public void testRefreshKeepsRoute() {
        WebDriver driver = openRoute("/#/about");
        driver.navigate().refresh();
        waitForContent(driver);
        Assert.assertTrue(driver.getCurrentUrl().contains("/#/about"),
                "Lỗi: Refresh làm mất route! URL hiện tại: " + driver.getCurrentUrl());
    }

    @Test(description = "Đường dẫn không tồn tại không làm trắng trang")
    public void testUnknownRouteDoesNotCrash() {
        WebDriver driver = openRoute("/#/this-page-does-not-exist");
        String bodyText = (String) ((JavascriptExecutor) driver)
                .executeScript("return document.body.innerText.trim()");
        Assert.assertFalse(bodyText.isEmpty(), "Lỗi: Trang bị trắng với đường dẫn không tồn tại!");
    }
}
