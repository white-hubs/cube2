package com.guhecloud.cube.srv.gen.maintain.server;

import com.guhecloud.platform.event.src.EventPublisher;
import com.guhecloud.platform.event.src.common.SysReadEvent;
import com.guhecloud.platform.event.src.entity.EventIdentity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;

/**
 * @author pengjian
 * @date 2022/4/22 14:51
 * @description
 */
@EnableFeignClients(basePackages = "com.guhecloud")
@SpringBootApplication(scanBasePackages = "com.guhecloud")
@ServletComponentScan(basePackages = "com.guhecloud.platform")
public class MaintainServer {
    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(MaintainServer.class, args);
        EventPublisher eventPublisher = applicationContext.getBean(EventPublisher.class);
        eventPublisher.publish(EventIdentity.createSysReadEvent(), new SysReadEvent());
    }
}
