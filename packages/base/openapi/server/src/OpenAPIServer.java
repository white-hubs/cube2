package com.guhecloud.cube.base.openapi.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "com.guhecloud")
@SpringBootApplication(scanBasePackages = "com.guhecloud")
@ServletComponentScan(basePackages = "com.guhecloud.platform")
public class OpenAPIServer {
    public static void main(String[] args) {
        SpringApplication.run(OpenAPIServer.class, args);
    }
}
