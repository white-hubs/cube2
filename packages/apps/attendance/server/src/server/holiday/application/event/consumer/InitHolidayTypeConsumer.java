package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.event.consumer;

import com.guhecloud.cube.biz.ouaa.client.message.TenantInitMessage;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.commandgateway.init.InitHolidayType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RocketMQMessageListener(
        consumerGroup = "initHolidayType",
        topic = TenantInitMessage.TOPIC,
        selectorType = SelectorType.TAG
)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InitHolidayTypeConsumer implements RocketMQListener<TenantInitMessage> {

    private final InitHolidayType initHolidayType;

    @Override
    public void onMessage(TenantInitMessage tenantInitMessage) {
        log.info("收到租户初始化消息{}--假期初始化开始", tenantInitMessage);
        initHolidayType.execute();
    }
}
