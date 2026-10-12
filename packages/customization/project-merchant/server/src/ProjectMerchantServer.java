package com.guhecloud.cube.deliver.project.merchant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author lijie
 * @date 2023/6/27 13:51
 * @description
 */
@EnableFeignClients(basePackages = "com.guhecloud")
@SpringBootApplication(scanBasePackages = "com.guhecloud")
@ServletComponentScan(basePackages = "com.guhecloud")
public class ProjectMerchantServer {
    public static void main(String[] args) {
        SpringApplication.run(ProjectMerchantServer.class, args);
    }
}
