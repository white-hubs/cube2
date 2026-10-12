package com.guhecloud.cube.srv.gen.attendance.management.server.leave.application.event.consumer;

import base.VariableConstant;
import com.guhecloud.cube.base.flow.client.FlowResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.leave.application.commandgateway.LeaveFormCommandService;
import instance.event.ProcessCompletedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 回滚预扣假期余额
 */
@Slf4j
@Component("HolidayUndoProcessConsumer")
@RocketMQMessageListener(
        consumerGroup = "HolidayUndoProcessConsumerGroup",
        topic = ProcessCompletedMessage.TOPIC,
        selectorExpression = "LEAVE"
)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UndoProcessConsumer implements RocketMQListener<ProcessCompletedMessage> {

    private final LeaveFormCommandService leaveFormCommandService;

    @Override
    public void onMessage(ProcessCompletedMessage processCompletedMessage) {
        Object leaveId = processCompletedMessage.getProperties().get(VariableConstant.preSubmitResponseKey);
        if (processCompletedMessage.getResult() == FlowResult.UNDO) {
            leaveFormCommandService.undo(Long.valueOf(leaveId.toString()));
        }
    }
}
