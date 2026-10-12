package com.guhecloud.cube.srv.gen.maintain.server.fault.event;

import com.guhecloud.cube.srv.gen.maintain.server.common.enumeration.MaintainApplicationEnum;
import com.guhecloud.cube.srv.gen.maintain.server.fault.commandgateway.FaultOrderService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.InspectRecordCommandService;
import instance.event.ProcessCompletedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wql
 * @describe 故障单审批完成消息
 * @date 2023/11/7 14:40
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "FAULT_ORDER_COMPLETED", topic = ProcessCompletedMessage.TOPIC,
                         selectorExpression = "FAULT || COMPLAINT"
)
public class FaultOrderCompleteEvent implements RocketMQListener<ProcessCompletedMessage> {

    private final FaultOrderService faultOrderService;
    private final InspectRecordCommandService inspectRecordCommandService;

    @Override
    public void onMessage(ProcessCompletedMessage message) {
        if (MaintainApplicationEnum.FAULT.name().equals(message.getBusinessId().getBusinessType())) {
            log.info("故障单流程审批结束:{},data:{},result:{}", message.getProcessInstanceId(), message.getData(), message.getResult()
                    .name());
            faultOrderService.updateResult(message.getProcessInstanceId(), message.getData(), message.getResult());
        } else if (MaintainApplicationEnum.COMPLAINT.name().equals(message.getBusinessId().getBusinessType())) {
            log.info("投诉建议单节点审批:{},data:{}", message.getProcessInstanceId(), message.getData());
            faultOrderService.updateComplaintOrder(message.getProcessInstanceId(), message.getData(), message.getResult().name());
        }
    }
}
