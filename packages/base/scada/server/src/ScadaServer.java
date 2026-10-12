package com.guhecloud.kaihe.scada.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "com.guhecloud")
@SpringBootApplication(scanBasePackages = "com.guhecloud")
@ServletComponentScan(basePackages = "com.guhecloud.platform")
public class ScadaServer {

//    @Bean
//    FilterRegistrationBean forwardFilter() {
//        return new FilterRegistrationBean(new ForwardedHeaderFilter());
//    }

    public static void main(String[] args) {
        SpringApplication.run(ScadaServer.class, args);
    }
}
