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
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        String testName = method.getName() + "_" + this.getClass().getSimpleName().replace("Test", "");

        if (System.getProperty("isRetry") != null && System.getProperty("isRetry").equals("true")) {
            System.out.println("🎥 ĐANG TÁI HIỆN BUG - QUAY VIDEO CHẬM...");
            VideoRecorder.startRecording(method.getName() + "_Reproduce");
        }
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        String pageName = this.getClass().getSimpleName().replace("Test", "");

        if (result.getStatus() == ITestResult.FAILURE) {
            CaptureHelpers.takeScreenshot(driver, result.getName(), pageName);
            System.out.println("📸 Đã đóng khung đỏ và chụp ảnh vị trí lỗi.");

            // 2. KIỂM TRA: Nếu case này phức tạp (có Retry) thì mới giữ Video
            if (result.getMethod().getRetryAnalyzer(result) != null) {
                VideoRecorder.stopAndKeepVideo();
                System.out.println("🎥 Case nhiều bước: Đã lưu video tái hiện bug.");
            } else {
                VideoRecorder.stopAndDeleteVideo();
            }
        } else {
            VideoRecorder.stopAndDeleteVideo();
        }
        driver.quit();
    }
}