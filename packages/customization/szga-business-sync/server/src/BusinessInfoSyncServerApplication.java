package com.guhecloud.cube.srv.gen.business.info.sync.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "com.guhecloud")
@ServletComponentScan(basePackages = "com.guhecloud.platform")
@SpringBootApplication(scanBasePackages = "com.guhecloud")
public class BusinessInfoSyncServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BusinessInfoSyncServerApplication.class, args);
    }

}
