package com.guhecloud.cube.srv.gen.park.management.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

@MapperScan({ "com.guhecloud.cube.**.dao", "com.guhecloud.cube.**.db.mapper" })
@EnableFeignClients(basePackages = "com.guhecloud")
@SpringBootApplication(scanBasePackages = "com.guhecloud")
@ServletComponentScan(basePackages = "com.guhecloud.platform")
public class ParkManagementServer {

    public static void main(String[] args) {
        SpringApplication.run(ParkManagementServer.class, args);
    }

}
