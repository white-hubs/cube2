package com.guhecloud.cube.srv.gen.disinfection.server;

import com.guhecloud.platform.event.src.EventPublisher;
import com.guhecloud.platform.event.src.common.SysReadEvent;
import com.guhecloud.platform.event.src.entity.EventIdentity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;

/**
 * @author ht
 * date  2025/7/15
 * description 消杀服务
 */
@SpringBootApplication(scanBasePackages = "com.guhecloud")
@EnableFeignClients(basePackages = "com.guhecloud")
@ServletComponentScan(basePackages = "com.guhecloud.platform")
public class DisinfectionServer {
    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(DisinfectionServer.class);
        EventPublisher eventPublisher = applicationContext.getBean(EventPublisher.class);
        eventPublisher.publish(EventIdentity.createSysReadEvent(), new SysReadEvent());
    }
}
