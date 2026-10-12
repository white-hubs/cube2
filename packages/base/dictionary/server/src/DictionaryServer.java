package com.guhecloud.cube.base.dictionary.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author lijie
 * @date 2023/6/27 13:51
 * @description
 */
@EnableFeignClients(basePackages = "com.guhecloud")
@SpringBootApplication(scanBasePackages = "com.guhecloud")
@ServletComponentScan(basePackages = "com.guhecloud.platform")
public class DictionaryServer {
    public static void main(String[] args) {
        SpringApplication.run(DictionaryServer.class, args);
    }
}
