package com.guhecloud.cube.srv.gen.conferencemanage.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = "com.guhecloud")
@EnableFeignClients(basePackages = "com.guhecloud")
@ServletComponentScan(basePackages = "com.guhecloud")
@ComponentScan(basePackages = { "com.guhecloud" })
public class ConferenceManageServer {
    public static void main(String[] args) {
        SpringApplication.run(ConferenceManageServer.class, args);
    }
}
