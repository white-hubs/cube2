package com.guhecloud.cube.srv.gen.maintain.server.fault.event;

import com.guhecloud.cube.srv.gen.maintain.server.fault.commandgateway.FaultOrderService;
import instance.event.ProcessDeleteMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/8/21 15:42
 * @Description:
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "FAULT_ORDER_DELETE", topic = ProcessDeleteMessage.TOPIC,
                         selectorExpression = "FAULT || COMPLAINT"
)
public class FaultOrderDeleteEvent implements RocketMQListener<ProcessDeleteMessage> {

    private final FaultOrderService faultOrderService;

    @Override
    public void onMessage(ProcessDeleteMessage message) {
        faultOrderService.deleteFaultOrder(message.getProcessInstanceId());
    }
}
