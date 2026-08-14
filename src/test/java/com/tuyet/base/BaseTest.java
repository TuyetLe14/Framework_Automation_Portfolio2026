package com.tuyet.base;

import com.tuyet.constants.ConfigsData;
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
        if (ConfigsData.VIDEO_ENABLED) {

            boolean shouldRecordVideo =
                    ConfigsData.VIDEO_RECORD_ALL
                            || "true".equalsIgnoreCase(
                            System.getProperty("isRetry", "false")
                    );
            if (shouldRecordVideo) {
                String videoName = method.getName();
                if ("true".equalsIgnoreCase(
                System.getProperty("isRetry", "false"))) {
                    videoName += "_Reproduce";
                    System.out.println(
                            "🎥 ĐANG TÁI HIỆN BUG - QUAY VIDEO..."
                    );
                }
                VideoRecorder.startRecording(videoName);
            }
        }
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        String pageName = this.getClass().getSimpleName().replace("Test", "");
        try {
            if (result.getStatus() == ITestResult.FAILURE && ConfigsData.SCREENSHOT_ENABLED
                    && ConfigsData.SCREENSHOT_ON_FAILURE ) {
                if (driver != null) {
                    CaptureHelpers.takeScreenshot(
                            driver,
                            result.getName(),
                            pageName
                    );
                    System.out.println(
                            "📸 Đã chụp ảnh vị trí lỗi tại trang: "
                                    + pageName
                    );
                }}
            if (ConfigsData.VIDEO_ENABLED) {
                if (result.getStatus() == ITestResult.FAILURE
                        && ConfigsData.VIDEO_ON_FAILURE) {
                    if (result.getMethod().getRetryAnalyzer(result) != null) {
                       VideoRecorder.stopAndKeepVideo();
                       System.out.println("🎥 Đã lưu video tái hiện bug.");
                    } else {
                       VideoRecorder.stopAndDeleteVideo();
                    }
                } else {
                   VideoRecorder.stopAndDeleteVideo();
                }
            }
        } finally {
            DriverManager.quitDriver();
            driver = null;
        }
    }
}
