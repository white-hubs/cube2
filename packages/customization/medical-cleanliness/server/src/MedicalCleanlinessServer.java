package com.guhecloud.cube.srv.gen.medical.cleanliness.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Author: lijie
 * @CreateTime: 2024-03-18  10:00
 * @Description:
 * @Version: 1.0
 */
@SpringBootApplication(scanBasePackages = "com.guhecloud")
@EnableFeignClients(basePackages = "com.guhecloud")
@ServletComponentScan(basePackages = "com.guhecloud.platform")
public class MedicalCleanlinessServer {
    public static void main(String[] args) {
        SpringApplication.run(MedicalCleanlinessServer.class);
    }

}
