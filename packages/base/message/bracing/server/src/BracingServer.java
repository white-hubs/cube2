package com.guhecloud.cube.base.bracing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author wql
 * @describe 基础服务支撑
 * @date 2023/9/1 14:25
 */
@SpringBootApplication(scanBasePackages = "com.guhecloud")
@EnableFeignClients(basePackages = "com.guhecloud")
@ServletComponentScan(basePackages = "com.guhecloud")
public class BracingServer {
    public static void main(String[] args) {
        SpringApplication.run(BracingServer.class);
    }
}