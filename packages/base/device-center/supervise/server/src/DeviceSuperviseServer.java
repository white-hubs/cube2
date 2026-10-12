package com.guhecloud.cube.srv.gen.device.supervise.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = "com.guhecloud")
@EnableFeignClients(basePackages = "com.guhecloud")
@ServletComponentScan(basePackages = "com.guhecloud")
@ComponentScan(basePackages = { "com.guhecloud" })
public class DeviceSuperviseServer {

    public static void main(String[] args) {
        SpringApplication.run(DeviceSuperviseServer.class, args);
    }

}
