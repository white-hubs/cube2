package com.guhecloud.cube.base.license.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication(scanBasePackages = "com.guhecloud", exclude = { DataSourceAutoConfiguration.class })
@ServletComponentScan(basePackages = { "com.guhecloud" })
public class LicenseServer {

    public static void main(String[] args) {
        SpringApplication.run(LicenseServer.class, args);
    }

}
