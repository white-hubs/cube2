package com.guhecloud.cube.base.visualization.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author lijie
 * @date 2023/8/14
 * @description
 */
@SpringBootApplication(scanBasePackages = "com.guhecloud")
@EnableFeignClients(basePackages = "com.guhecloud")
@ServletComponentScan(basePackages = "com.guhecloud.platform")
public class VisualizationServer {
    public static void main(String[] args) {
        SpringApplication.run(VisualizationServer.class);
    }
}
