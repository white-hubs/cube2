package com.guhecloud.cube.biz.subsystem.parking.server.Illegal.infrastructure.rocketmq;

import com.guhecloud.cube.base.flow.client.FlowResult;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.application.commandgetway.IllegalParkingCommandService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import instance.event.ProcessCompletedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/6 14:02
 * @Description: 车辆违停审批通过处理
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "ILLEGAL_PARKING_COMPLETE_GROUP", topic = ProcessCompletedMessage.TOPIC,
                         selectorExpression = "ILLEGAL_PARKING"
)
public class IllegalParkingApprovalCompleteConsumer implements RocketMQListener<ProcessCompletedMessage> {
    private final IllegalParkingCommandService illegalParkingCommandService;

    @Override
    public void onMessage(ProcessCompletedMessage message) {
        try {
            SessionContextHolder.requestTenant(message.getTenant());
            if (FlowResult.PASS != message.getResult()) {
                return;
            }
            illegalParkingCommandService.businessOperation(message);
        } catch (Exception e) {
            log.error("车辆违停业务处理异常:{}", e.getMessage(), e);
        }
    }
}
