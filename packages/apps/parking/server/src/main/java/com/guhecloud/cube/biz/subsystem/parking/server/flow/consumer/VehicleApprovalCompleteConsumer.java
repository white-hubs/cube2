package com.guhecloud.cube.biz.subsystem.parking.server.flow.consumer;

import com.guhecloud.cube.base.flow.client.FlowResult;
import com.guhecloud.cube.biz.subsystem.parking.server.flow.service.VehicleApprovalService;
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
 * @Date: 2024/12/11 15:22
 * @Description: 车辆审批结束业务监听
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "VEHICLE_APPROVAL_COMPLETE", topic = ProcessCompletedMessage.TOPIC,
                         selectorExpression = "VECHICLE_INSERT || VECHICLE_UPDATE || VECHICLE_DELETE"
)
public class VehicleApprovalCompleteConsumer implements RocketMQListener<ProcessCompletedMessage> {
    private final VehicleApprovalService vehicleApprovalService;

    @Override
    public void onMessage(ProcessCompletedMessage message) {
        log.info("车辆流程审批结束:{},data:{},result:{}", message.getProcessInstanceId(), message.getData(), message.getResult().name());
        try {
            SessionContextHolder.requestTenant(message.getTenant());
            if (FlowResult.PASS != message.getResult()) {
                return;
            }
            vehicleApprovalService.businessOperation(message);
        } catch (Exception e) {
            log.error("车辆审批结束业务处理异常:{}", e.getMessage());
        }
    }
}
