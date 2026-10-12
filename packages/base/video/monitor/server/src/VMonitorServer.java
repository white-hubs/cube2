package com.guhecloud.cube.biz.vmonitor.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author lanchun
 * @date 2021/12/6 17:08
 */
@SpringBootApplication(scanBasePackages = "com.guhecloud")
@EnableFeignClients(basePackages = "com.guhecloud")
@ServletComponentScan(basePackages = "com.guhecloud")
public class VMonitorServer {

    public static void main(String[] args) {
        SpringApplication.run(VMonitorServer.class, args);
    }

}
