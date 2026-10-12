package com.guhecloud.cube.srv.gen.content.management.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author lzy
 * @date 2022/5/23
 */
@SpringBootApplication(scanBasePackages = "com.guhecloud")
@EnableFeignClients(basePackages = "com.guhecloud")
@ServletComponentScan(basePackages = "com.guhecloud")
public class ContentManagementServer {

    public static void main(String[] args) {
        SpringApplication.run(ContentManagementServer.class, args);
    }
}
