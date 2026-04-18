package com.tuyet.tests;

import com.tuyet.base.BaseTest;
import org.testng.annotations.Test;

public class OpenWeb extends BaseTest {

    @Test
    public void PortfolioTest() {
        driver.get(" https://anh-tuyet-portfolio.tgdd-ld9941.workers.dev/");
        System.out.println("Title is: " + driver.getTitle());
    }
}