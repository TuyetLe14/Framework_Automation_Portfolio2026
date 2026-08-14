package com.tuyet.base;

import com.tuyet.utils.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.ITestResult;
import org.testng.annotations.*;
import java.lang.reflect.Method;
import java.time.Duration;

public class BaseTest {
    protected WebDriver driver;

    public WebDriver getDriver() {
        return driver;
    }

    @BeforeMethod
    public void createDriver(Method method) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        // Fix lỗi: Thêm headless nếu cần chạy CI/CD sau này
        // options.addArguments("--headless=new");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));

        if ("true".equals(System.getProperty("isRetry"))) {
            System.out.println("🎥 ĐANG TÁI HIỆN BUG - QUAY VIDEO CHẬM...");
            VideoRecorder.startRecording(method.getName() + "_Reproduce");
        }
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        String pageName = this.getClass().getSimpleName().replace("Test", "");

        if (result.getStatus() == ITestResult.FAILURE) {
                CaptureHelpers.takeScreenshot(driver, result.getName(), pageName);
                System.out.println("📸 Đã chụp ảnh vị trí lỗi tại trang: " + pageName);
            if (result.getMethod().getRetryAnalyzer(result) != null) {
                VideoRecorder.stopAndKeepVideo();
                System.out.println("🎥 Đã lưu video tái hiện bug.");
            } else {
                VideoRecorder.stopAndDeleteVideo();
            }
        } else {
            VideoRecorder.stopAndDeleteVideo();
        }

        if (driver != null) {
            driver.quit();
        }
    }
}