package com.guhecloud.cube.srv.gen.official.management.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author appet
 * @date 2023/2/17 13:54
 * @description
 */
@SpringBootApplication(scanBasePackages = "com.guhecloud")
@EnableFeignClients(basePackages = "com.guhecloud")
@ServletComponentScan(basePackages = "com.guhecloud.platform")
public class OfficialManagementServer {
    public static void main(String[] args) {
        SpringApplication.run(OfficialManagementServer.class);
    }
}
