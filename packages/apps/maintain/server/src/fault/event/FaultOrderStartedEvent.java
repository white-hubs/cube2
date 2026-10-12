package com.guhecloud.cube.srv.gen.maintain.server.fault.event;

import com.guhecloud.cube.srv.gen.maintain.server.common.enumeration.MaintainApplicationEnum;
import com.guhecloud.cube.srv.gen.maintain.server.fault.commandgateway.FaultOrderService;
import instance.command.ProcessStartedMessageAttrCommand;
import instance.event.ProcessStartedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wql
 * @describe 故障单流程发起成功消息监听
 * @date 2023/11/6 9:41
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "FAULT_ORDER_APPLY_STARTED", topic = ProcessStartedMessage.TOPIC,
                         selectorExpression = "FAULT || COMPLAINT"
)
public class FaultOrderStartedEvent implements RocketMQListener<ProcessStartedMessageAttrCommand> {
    private final FaultOrderService faultOrderService;

    @Override
    public void onMessage(ProcessStartedMessageAttrCommand message) {
        if (MaintainApplicationEnum.FAULT.name().equals(message.getBusinessType())) {
            log.info("故障单流程发起成功:{},data:{}", message.getProcessInstanceId(), message.getData());
            faultOrderService.addFaultOrder(message);
        } else if (MaintainApplicationEnum.COMPLAINT.name().equals(message.getBusinessType())) {
            log.info("投诉建议流程发起成功:{},data:{}", message.getProcessInstanceId(), message.getData());
            faultOrderService.addComplaintOrder(message);
        }
    }
}
