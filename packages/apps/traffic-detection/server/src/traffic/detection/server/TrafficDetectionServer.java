package com.guhecloud.cube.biz.traffic.detection.server;

import com.guhecloud.platform.event.src.EventPublisher;
import com.guhecloud.platform.event.src.common.SysReadEvent;
import com.guhecloud.platform.event.src.entity.EventIdentity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;

@SpringBootApplication(scanBasePackages = "com.guhecloud")
@EnableFeignClients(basePackages = "com.guhecloud")
@ServletComponentScan(basePackages = "com.guhecloud")
public class TrafficDetectionServer {
    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(TrafficDetectionServer.class);
        EventPublisher eventPublisher = applicationContext.getBean(EventPublisher.class);
        eventPublisher.publish(EventIdentity.createSysReadEvent(), new SysReadEvent());
    }
}
