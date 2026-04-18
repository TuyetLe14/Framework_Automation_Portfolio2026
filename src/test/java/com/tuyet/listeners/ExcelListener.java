package com.tuyet.listeners;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import com.tuyet.base.BaseTest;
import com.tuyet.utils.CaptureHelpers;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ExcelListener implements ITestListener {
    private Workbook workbook;
    private Sheet sheet;
    private List<TestResultData> results = new ArrayList<>();

    private static class TestResultData {
        String name, status, desc, page;

        TestResultData(String n, String s, String d, String p) {
            name = n;
            status = s;
            desc = d;
            page = p;
        }
    }

    private String getPageName(ITestResult result) {
        String className = result.getTestClass().getRealClass().getSimpleName();
        return className.replace("Test", "");
    }

    @Override
    public void onStart(ITestContext context) {
        results.clear(); 
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("Kết quả Test");

        CellStyle headerStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        headerStyle.setFont(font);

        Row header = sheet.createRow(0);
        String[] columns = { "Tên Test Case", "Trang (Page)", "Trạng Thái", "Mô Tả" };
        for (int i = 0; i < columns.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerStyle);
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        results.add(new TestResultData(result.getName(), "PASSED", "Success", getPageName(result)));
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String pageName = getPageName(result);
        results.add(new TestResultData(result.getName(), "FAILED", result.getThrowable().getMessage(), pageName));
        Object testClass = result.getInstance();
        if (testClass instanceof BaseTest) {
            WebDriver driver = ((BaseTest) testClass).getDriver();
            if (driver != null)
                CaptureHelpers.takeScreenshot(driver, result.getName(), pageName);
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        File mainFolder = new File("Test_Reports");
        String dateFolderStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        File subFolder = new File(mainFolder, dateFolderStr);

        if (!subFolder.exists()) {
            subFolder.mkdirs();
        } else {
            File[] files = subFolder.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.isFile() && f.getName().endsWith(".xlsx")) {
                        if (!f.delete()) {
                            f.renameTo(new File(f.getAbsolutePath() + ".old"));
                        }
                    }
                }
            }
        }
        String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HHmmss"));
        String filePath = subFolder.getAbsolutePath() + File.separator + "BaoCao_" + timeStamp + ".xlsx";

        System.out.println("---- ĐANG XUẤT BÁO CÁO MỚI: " + filePath + " ----");

        int rowNum = 1;
        for (TestResultData data : results) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(data.name);
            row.createCell(1).setCellValue(data.page);
            row.createCell(2).setCellValue(data.status);
            row.createCell(3).setCellValue(data.desc);
        }

        for (int i = 0; i < 4; i++)
            sheet.autoSizeColumn(i);

        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
            fileOut.flush(); 
        } catch (IOException e) {
            System.err.println("Lỗi ghi file: " + e.getMessage());
        } finally {
            try {
                if (workbook != null) {
                    workbook.close(); 
                    System.out.println("✅ Đã đóng Workbook và dọn dẹp file cũ thành công!");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}