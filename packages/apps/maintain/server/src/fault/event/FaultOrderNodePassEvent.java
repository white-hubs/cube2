package com.guhecloud.cube.srv.gen.maintain.server.fault.event;

import com.guhecloud.cube.srv.gen.maintain.server.common.enumeration.MaintainApplicationEnum;
import com.guhecloud.cube.srv.gen.maintain.server.common.enumeration.TicketStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.fault.commandgateway.FaultOrderService;
import instance.event.NodePassMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wql
 * @describe 故障单节点审批通过消息
 * @date 2023/11/6 10:51
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "FAULT_ORDER_NODE_PASS_LJ", topic = NodePassMessage.TOPIC,
                         selectorExpression = "FAULT || COMPLAINT"
)
public class FaultOrderNodePassEvent implements RocketMQListener<NodePassMessage> {
    private final FaultOrderService faultOrderService;

    @Override
    public void onMessage(NodePassMessage message) {
        if (MaintainApplicationEnum.FAULT.name().equals(message.getBusinessType())) {
            log.info("故障单节点审批通过:{},data:{}", message.getProcessInstanceId(), message.getData());
            faultOrderService.updateFaultOrder(message);
        } else if (MaintainApplicationEnum.COMPLAINT.name().equals(message.getBusinessType())) {
            log.info("投诉建议单节点审批:{},data:{}", message.getProcessInstanceId(), message.getData());
            faultOrderService.updateComplaintOrder(message.getProcessInstanceId(), message.getData(), TicketStatusEnum.WAITING_FOR_HANDLE.name());
        }
    }
}
