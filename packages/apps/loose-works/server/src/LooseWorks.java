package com.guhecloud.cube.srv.gen.loose.works;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author wql
 * @describe 小散工程
 * @date 2023/9/14 9:14
 */
@SpringBootApplication(scanBasePackages = "com.guhecloud")
@EnableFeignClients(basePackages = "com.guhecloud")
@ServletComponentScan(basePackages = "com.guhecloud")
public class LooseWorks {
    public static void main(String[] args) {
        SpringApplication.run(LooseWorks.class);
    }
}