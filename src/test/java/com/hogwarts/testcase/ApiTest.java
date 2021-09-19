package com.hogwarts.testcase;

import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.config.SSLConfig;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Properties;

public class ApiTest {
    private String baseUrl = "";
    private String expectCityName = "";

    /**
     * Get the city name from the response body.
     *
     * @param cityCode: city code (string)
     * @return: City name (String)
     */
    @Step
    private String getCityName(String cityCode) {
        String fullUrl = baseUrl + cityCode + ".html";
        Response resp = RestAssured
                .given()
                .config((RestAssured.config().sslConfig(new SSLConfig().relaxedHTTPSValidation())))
                .get(fullUrl);
        String cur_encoding = Charset.defaultCharset().name();
        String city = null;
        try {
            String s_temp = new String(resp.getBody().asString().getBytes(cur_encoding), "UTF-8");
            System.out.println(s_temp);
            city = new String(((String) resp.jsonPath().get("weatherinfo.city")).getBytes(cur_encoding), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return city;
    }

    @BeforeEach
    public void begin() {
        String host = "www.weather.com.cn";
        baseUrl = "http://" + host + "/data/cityinfo/";
    }

    @AfterEach
    public void tearDown() {
        System.out.println(expectCityName + " Test Finished!");
    }

    @Test
    @Feature("Test ShenZhen")
    public void testShenZhen() {
        expectCityName = "深圳";
        String actualCityName = getCityName("101280601");
        Assertions.assertEquals(expectCityName, actualCityName);
    }

    @Test
    @Feature("Test ShangHai")
    public void testShangHai() {
        expectCityName = "上海";
        String actualCityName = getCityName("101020100");
        Assertions.assertEquals(expectCityName, actualCityName);
    }

    @Test
    @Feature("Test Beijing")
    public void testBeijing() {
        expectCityName = "北京";
        String actualCityName = getCityName("101010100");
        Assertions.assertEquals(expectCityName, actualCityName);
    }

}
