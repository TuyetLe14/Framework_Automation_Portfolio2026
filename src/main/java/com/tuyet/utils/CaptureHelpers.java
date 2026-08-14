package com.tuyet.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CaptureHelpers {
    public static String takeScreenshot(WebDriver driver, String screenshotName, String folderName) {
        try {
            String dateFolder = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            // Lưu vào: Test_Reports/2026-04-22/Screenshots/
            String path = "Test_Reports/" + dateFolder + "/Screenshots/" + folderName + "_" + screenshotName + ".png";

            File file = new File(path);
            if (!file.getParentFile().exists())
                file.getParentFile().mkdirs();

            File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            org.openqa.selenium.io.FileHandler.copy(source, file);

            System.out.println("📸 Screenshot saved at: " + file.getAbsolutePath());
            return file.getAbsolutePath(); // BẮT BUỘC trả về Absolute Path để Excel không bị lạc đường
        } catch (Exception e) {
            return "";
        }
    }
}