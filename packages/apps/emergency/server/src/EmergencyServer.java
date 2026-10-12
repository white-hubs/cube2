package com.guhecloud.cube.biz.emergency.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "com.guhecloud")
@EnableFeignClients(basePackages = "com.guhecloud")
@ServletComponentScan(basePackages = { "com.guhecloud" })
public class EmergencyServer {
    public static void main(String[] args) {
        SpringApplication.run(EmergencyServer.class, args);
    }
}
