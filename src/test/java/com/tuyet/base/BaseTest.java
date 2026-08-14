package com.tuyet.base;

import com.tuyet.utils.CaptureHelpers;
import com.tuyet.utils.VideoRecorder;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import java.lang.reflect.Method;

public class BaseTest {
    protected WebDriver driver;

    public WebDriver getDriver() {
        return driver;
    }

    @BeforeMethod
    public void createDriver(Method method) {
        DriverManager.createDriver();
        driver = DriverManager.getDriver();

        if ("true".equalsIgnoreCase(
                System.getProperty("isRetry", "false"))) {

            System.out.println(
                    "🎥 ĐANG TÁI HIỆN BUG - QUAY VIDEO CHẬM..."
            );

            VideoRecorder.startRecording(
                    method.getName() + "_Reproduce"
            );
        }
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        String pageName = this.getClass().getSimpleName().replace("Test", "");
        try {
            if (result.getStatus() == ITestResult.FAILURE) {
                if (drive != null) {
                    CaptureHelpers.takeScreenshot(
                            driver,
                            result.getName(),
                            pageName
                    );
                    System.out.println(
                            "📸 Đã chụp ảnh vị trí lỗi tại trang: "
                                    + pageName
                    );
                }
                if (result.getMethod().getRetryAnalyzer(result) != null) {
                    VideoRecorder.stopAndKeepVideo();
                    System.out.println("🎥 Đã lưu video tái hiện bug.");
                } else {
                    VideoRecorder.stopAndDeleteVideo();
                }
            } else {
              VideoRecorder.stopAndDeleteVideo();
            }
        } finally {
            DriverManager.quitDriver();
            driver = null;
        }
    }
}
