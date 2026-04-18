package com.tuyet.utils;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryListener implements IRetryAnalyzer {
    private int count = 0;
    private static final int MAX_RETRY = 1; 

    @Override
    public boolean retry(ITestResult result) {
        if (count < MAX_RETRY) {
            count++;
            return true; 
        }
        return false;
    }
}