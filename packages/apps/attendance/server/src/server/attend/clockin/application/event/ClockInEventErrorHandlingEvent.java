package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.application.event;

import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.base.flow.client.FlowResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.application.commandgateway.AttClockInLedgerApplication;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.repository.AttApproveLogRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.repository.AttClockInRecordsRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttClockInRecordsPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.repairclock.commandgateway.command.ClockRepairCommand;
import com.guhecloud.platform.base.util.ObjectUtils;
import instance.event.ProcessCompletedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: lijie
 * @CreateTime: 2023-10-09  15:41
 * @Description: 补卡审批通过后，消费补卡信息
 * @Version: 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = ProcessCompletedMessage.TOPIC + "_CLOCK_OMIT", topic = ProcessCompletedMessage.TOPIC,
                         selectorExpression = "REPAIR_CLOCK"
)
public class ClockInEventErrorHandlingEvent implements RocketMQListener<ProcessCompletedMessage> {
    private final AttClockInRecordsRepository clockInRecordsRepository;
    private final AttClockInLedgerApplication ledgerApplication;
    private final AttApproveLogRepository approveLogRepository;

    @Override
    public void onMessage(ProcessCompletedMessage message) {
        log.info("补卡审批通过：{}", JSONObject.toJSONString(message));
        ClockRepairCommand command = JSONObject.parseObject(message.getData(), ClockRepairCommand.class);
        String status = FlowResult.PASS.name().equals(message.getResult().name()) ? FlowResult.FINISHED.name() : message.getResult().name();
        Boolean bool = approveLogRepository.updateApproveLog(status, message.getProcessInstanceId());
        if (bool && FlowResult.FINISHED.name().equals(status)) {
            AttClockInRecordsPO clock = clockInRecordsRepository.updateClockIn(command.getClassesId());
            if (ObjectUtils.isNotEmpty(clock)) {
                ledgerApplication.updateLedgerDetails(clock);
            }
        }
    }
}
