package com.guhecloud.cube.biz.fusion.communicate.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 通讯服务
 *
 * @author lanchun
 * @date 2024/7/29 9:37
 */
@EnableFeignClients(basePackages = "com.guhecloud")
@SpringBootApplication(scanBasePackages = "com.guhecloud")
@ServletComponentScan(basePackages = "com.guhecloud.platform")
public class FusionCommunicateServer {
    public static void main(String[] args) {
        SpringApplication.run(FusionCommunicateServer.class, args);
    }
}
