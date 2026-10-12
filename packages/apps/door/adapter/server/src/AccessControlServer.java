package com.guhecloud.cube.srv.gen.adapter.access.control.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "com.guhecloud")
@EnableFeignClients(basePackages = "com.guhecloud")
@ServletComponentScan(basePackages = "com.guhecloud.platform")
public class AccessControlServer {
    public static void main(String[] args) {
        SpringApplication.run(AccessControlServer.class, args);
    }
}
