package com.guhecloud.cube.srv.gen.adapter.onecard.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "com.guhecloud")
@EnableFeignClients(basePackages = "com.guhecloud")
@ServletComponentScan(basePackages = "com.guhecloud")
public class OneCardAdapterServer {
    public static void main(String[] args) {
        SpringApplication.run(OneCardAdapterServer.class, args);
    }
}
