package com.guhecloud.cube.biz.dispatch.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author appet
 * @date 2022/7/26 10:18
 * @description
 */
@EnableFeignClients(basePackages = "com.guhecloud")
@SpringBootApplication(scanBasePackages = "com.guhecloud")
@ServletComponentScan(basePackages = "com.guhecloud.platform")
public class DispatchServer {
    public static void main(String[] args) {
        SpringApplication.run(DispatchServer.class, args);
    }
}
