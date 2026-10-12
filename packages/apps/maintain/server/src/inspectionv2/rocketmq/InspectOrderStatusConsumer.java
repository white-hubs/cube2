package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.rocketmq;

import com.guhecloud.cube.base.flow.client.FlowResult;
import com.guhecloud.cube.srv.gen.maintain.server.common.enumeration.MaintainApplicationEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.InspectRecordCommandService;
import instance.event.ProcessCompletedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 巡检工单监听(隐患单和故障单)
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "INSPECT_ORDER_COMPLETED", topic = ProcessCompletedMessage.TOPIC,
                         selectorExpression = "FAULT || HIDDENDANGER"
)
public class InspectOrderStatusConsumer implements RocketMQListener<ProcessCompletedMessage> {

    private final InspectRecordCommandService inspectRecordCommandService;

    @Override
    public void onMessage(ProcessCompletedMessage message) {
        if (MaintainApplicationEnum.FAULT.name().equals(message.getBusinessId().getBusinessType()) ||
            MaintainApplicationEnum.HIDDENDANGER.name().equals(message.getBusinessId().getBusinessType())) {
            log.info("巡检单流程审批结束:{},data:{},result:{}", message.getProcessInstanceId(), message.getData(), message.getResult()
                    .name());
            if (FlowResult.FINISHED.equals(message.getResult()) || FlowResult.PASS.equals(message.getResult())) {
                try {
                    // 处理记录和任务状态
                    inspectRecordCommandService.orderCompleteCallback(message.getProcessInstanceId());
                } catch (Exception e) {
                    log.error("巡检工单完成，回调出错：", e);
                }
            }
        }
    }
}
