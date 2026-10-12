package com.guhecloud.cube.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = { "com.guhecloud" }, exclude = { DataSourceAutoConfiguration.class })
@EnableDiscoveryClient
public class Gateway {

    public static void main(String[] args) {
        SpringApplication.run(Gateway.class, args);
        System.out.println("--------------网关-----------------------------------");
    }

}
