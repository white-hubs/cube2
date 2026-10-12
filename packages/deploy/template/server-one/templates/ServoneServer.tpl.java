package com.guhecloud.cube.servone;

import com.guhecloud.cube.servone.config.DynamicDataSourceInterceptor;
import com.guhecloud.cube.servone.config.MultiserverNacosConfigurer;
import com.guhecloud.platform.event.src.EventPublisher;
import com.guhecloud.platform.event.src.common.SysReadEvent;
import com.guhecloud.platform.event.src.entity.EventIdentity;
import com.mzt.logapi.starter.annotation.EnableLogRecord;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.context.ApplicationContext;

@MapperScan({ "com.guhecloud.cube.**.dao", "com.guhecloud.cube.**.mapper" })
@EnableFeignClients(basePackages = { "com.guhecloud" })
@ServletComponentScan(basePackages = { "com.guhecloud" })
@SpringBootApplication(scanBasePackages = { "com.guhecloud", "com.mzt", })
@EnableLogRecord(tenant = "com.guhecloud")
@EnableTransactionManagement(proxyTargetClass = true)
@ComponentScan(basePackages = { "com.github.tobato.fastdfs","com.guhecloud" }, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
                // @formatter:off
                {DEP_MAIN_CLASS_IMPORTS}
                // @formatter:on
        }
        )
}
)
@Import({ MultiserverNacosConfigurer.class, DynamicDataSourceInterceptor.class })
public class ServoneServer {
    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(ServoneServer.class);
        EventPublisher eventPublisher = applicationContext.getBean(EventPublisher.class);
        eventPublisher.publish(EventIdentity.createSysReadEvent(), new SysReadEvent());
        System.out.println("----------------------------------------ServoneServer启动成功-------------------------------------------");
    }
}