package com.guhecloud.cube.srv.gen.library.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "com.guhecloud")
@EnableFeignClients(basePackages = "com.guhecloud")
@ServletComponentScan(basePackages = "com.guhecloud")
/**
 * @author chx
 * @since since
 * @Created on Created
 */
public class LibraryManagementServer {

    public static void main(String[] args) {
        SpringApplication.run(LibraryManagementServer.class, args);
    }
}
