package com.guhecloud.cube.biz.ouaa.server;

import com.guhecloud.platform.event.src.EventPublisher;
import com.guhecloud.platform.event.src.common.SysReadEvent;
import com.guhecloud.platform.event.src.entity.EventIdentity;
import com.mzt.logapi.starter.annotation.EnableLogRecord;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;

/**
 * 服务启动类
 *
 * @author Woody
 * @date 2021/6/1
 */
@SpringBootApplication(scanBasePackages = "com.guhecloud")
@EnableLogRecord(tenant = "com.guhecloud")
@EnableFeignClients(basePackages = "com.guhecloud")
@ServletComponentScan(basePackages = "com.guhecloud")
public class OUAAServer {
    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(OUAAServer.class, args);
        EventPublisher eventPublisher = applicationContext.getBean(EventPublisher.class);
        eventPublisher.publish(EventIdentity.createSysReadEvent(), new SysReadEvent());
    }
}
