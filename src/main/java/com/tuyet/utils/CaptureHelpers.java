package com.tuyet.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.io.FileHandler;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CaptureHelpers {
    public static void takeScreenshot(WebDriver driver, String testCaseName, String pageName) {
        try {
            String dateFolder = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH-mm-ss"));
            File theDir = new File("screenshots/" + dateFolder);
            if (!theDir.exists()) theDir.mkdirs();

            String fileName = testCaseName + "_" + pageName + "_" + timeStamp + ".png";
            File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileHandler.copy(source, new File(theDir + "/" + fileName));
            System.out.println("📸 Screenshot: " + fileName);
        } catch (Exception e) { e.printStackTrace(); }
    }
}