package com.guhecloud.cube.srv.gen.safety.personnel.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 安全生产统一智管平台人员管理服务启动入口。
 */
@SpringBootApplication(scanBasePackages = "com.guhecloud")
@EnableFeignClients(basePackages = "com.guhecloud")
@ServletComponentScan(basePackages = { "com.guhecloud" })
public class SafetyPersonnelServer {

    public static void main(String[] args) {
        SpringApplication.run(SafetyPersonnelServer.class, args);
    }
}
