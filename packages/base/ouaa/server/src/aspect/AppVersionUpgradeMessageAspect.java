package com.guhecloud.cube.biz.ouaa.server.aspect;

import com.guhecloud.cube.biz.ouaa.server.rocketmq.message.AppVersionUpgradeMessage;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class AppVersionUpgradeMessageAspect {

    @Pointcut("execution(* com.guhecloud.cube.biz.ouaa.server.controller.admin.AppVersionController.save(..))")
    public void point() {

    }

    @AfterReturning(value = "point()", returning = "res")
    public void refreshAspect(JoinPoint joinPoint, Object res) {
        RocketMQContextHolder.send(AppVersionUpgradeMessage.TOPIC, AppVersionUpgradeMessage.builder()
                .id((Long) res)
                .build());
    }
}
