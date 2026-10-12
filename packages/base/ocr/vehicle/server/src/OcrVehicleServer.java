package com.guhecloud.base.ocr.vehicle.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "com.guhecloud")
@SpringBootApplication(scanBasePackages = "com.guhecloud")
@ServletComponentScan(basePackages = "com.guhecloud.platform")
public class OcrVehicleServer {
    public static void main(String[] args) {
        SpringApplication.run(OcrVehicleServer.class);
    }
}
