package com.guhecloud.cube.biz.daily.task;

import com.guhecloud.platform.event.src.EventPublisher;
import com.guhecloud.platform.event.src.common.SysReadEvent;
import com.guhecloud.platform.event.src.entity.EventIdentity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;

/**
 * @describe 日常任务服务
 * @date 2025/10/14 9:14
 */
@SpringBootApplication(scanBasePackages = "com.guhecloud")
@EnableFeignClients(basePackages = "com.guhecloud")
@ServletComponentScan(basePackages = "com.guhecloud")
public class DailyTaskServer {
    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(DailyTaskServer.class);
        EventPublisher eventPublisher = applicationContext.getBean(EventPublisher.class);
        eventPublisher.publish(EventIdentity.createSysReadEvent(), new SysReadEvent());
    }
}