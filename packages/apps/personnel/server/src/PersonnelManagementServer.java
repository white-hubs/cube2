package com.guhecloud.cube.srv.gen.personnel.management.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author pengjian
 * @date 2022/8/22 13:54
 * @description
 */
@SpringBootApplication(scanBasePackages = "com.guhecloud")
@EnableFeignClients(basePackages = "com.guhecloud")
@ServletComponentScan(basePackages = "com.guhecloud.platform")
public class PersonnelManagementServer {
    public static void main(String[] args) {
        SpringApplication.run(PersonnelManagementServer.class);
    }
}
