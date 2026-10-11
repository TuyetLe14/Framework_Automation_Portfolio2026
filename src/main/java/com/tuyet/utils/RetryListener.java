package com.tuyet.utils;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryListener implements IRetryAnalyzer {
    private static final Logger log = LoggerFactory.getLogger(RetryListener.class);
    private int count = 0;
    private static final int MAX_RETRY = 1; 

    @Override
    public boolean retry(ITestResult result) {
        Throwable t = result.getThrowable();
        boolean flaky = t instanceof StaleElementReferenceException
                || t instanceof TimeoutException
                || t instanceof NoSuchElementException
                || t instanceof WebDriverException;
        if (flaky && count < MAX_RETRY) {
            count++;
            log.warn("Retry {}/{} for test {} because of {}",
                    count, MAX_RETRY, result.getName(), t.getClass().getSimpleName());
            return true; 
        }
        return false;
    }
}
