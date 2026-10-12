package com.guhecloud.cube.srv.electron.guide;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Author: lijie
 * @CreateTime: 2024-01-05  16:29
 * @Description: 电子导览
 * @Version: 1.0
 */
@SpringBootApplication(scanBasePackages = "com.guhecloud")
@EnableFeignClients(basePackages = "com.guhecloud")
@ServletComponentScan(basePackages = "com.guhecloud.platform")
public class ElectronGuideServer {
    public static void main(String[] args) {
        SpringApplication.run(ElectronGuideServer.class);
    }

}
