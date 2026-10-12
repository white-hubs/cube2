package com.guhecloud.cube.biz.adapter.person.rollcall.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "com.guhecloud", exclude = { DataSourceAutoConfiguration.class })
@EnableFeignClients(basePackages = "com.guhecloud")
@ServletComponentScan(basePackages = { "com.guhecloud" })
public class AdapterRollCallServer {

    public static void main(String[] args) {
        SpringApplication.run(AdapterRollCallServer.class, args);
    }
}
