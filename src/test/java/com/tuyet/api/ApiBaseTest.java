package com.tuyet.api;

import com.tuyet.constants.ConfigsData;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;

public abstract class ApiBaseTest {

    protected RequestSpecification requestSpec;
    protected int maxResponseTimeMs;

    @BeforeClass(alwaysRun = true)
    public void setUpApi() {
        String baseUri = System.getProperty("api.baseUri",
                ConfigsData.getProperty("api.baseurl",
                        "https://jsonplaceholder.typicode.com"));
        maxResponseTimeMs = ConfigsData.getIntProperty("api.timeout", 5000);

        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        requestSpec = new RequestSpecBuilder()
                .setBaseUri(baseUri)
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addHeader("User-Agent", "qa-automation-portfolio")
                .build();
    }
}
