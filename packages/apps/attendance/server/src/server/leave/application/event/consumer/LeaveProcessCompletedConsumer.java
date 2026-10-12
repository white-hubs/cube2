package com.guhecloud.cube.srv.gen.attendance.management.server.leave.application.event.consumer;

import base.VariableConstant;
import com.guhecloud.cube.base.flow.client.FlowResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.repository.AttApproveLogRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttApproveLogPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.leave.application.commandgateway.LeaveFormCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.leave.application.commandgateway.command.LeaveApprovedCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.leave.application.commandgateway.command.LeaveRefuseCommand;
import instance.event.ProcessCompletedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author pengjian
 * @date 2022/11/18 19:30
 * @description 流程完成监听事件（请假审批通过）
 */
@Slf4j
@Component
@RocketMQMessageListener(
        consumerGroup = ProcessCompletedMessage.TOPIC + "_LEAVE",
        topic = ProcessCompletedMessage.TOPIC,
        selectorExpression = "LEAVE",
        selectorType = SelectorType.TAG
)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LeaveProcessCompletedConsumer implements RocketMQListener<ProcessCompletedMessage> {

    private final LeaveFormCommandService leaveFormCommandService;
    private final AttApproveLogRepository approveLogRepository;

    @Override
    public void onMessage(ProcessCompletedMessage processCompletedMessage) {
        Object leaveId = processCompletedMessage.getProperties().get(VariableConstant.preSubmitResponseKey);
        log.info("操作状态：{}", processCompletedMessage.getResult());
        if (processCompletedMessage.getResult() == FlowResult.PASS) {
            LeaveApprovedCommand leaveApprovedCommand = new LeaveApprovedCommand(Long.valueOf(leaveId.toString()));
            leaveFormCommandService.approved(leaveApprovedCommand);
        } else if (processCompletedMessage.getResult() == FlowResult.REJECT) {
            leaveFormCommandService.refuse(new LeaveRefuseCommand(Long.valueOf(leaveId.toString())));
        }
        AttApproveLogPO attApproveLog = new AttApproveLogPO();
        attApproveLog.setProcessInstanceId(processCompletedMessage.getProcessInstanceId());
        attApproveLog.setStatus(FlowResult.PASS.name().equals(processCompletedMessage.getResult().name()) ? FlowResult.FINISHED.name()
                                                                                                          : processCompletedMessage
                                        .getResult().name());
        approveLogRepository.updateApproveLog(attApproveLog);
    }
}
