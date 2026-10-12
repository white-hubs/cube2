package com.guhecloud.cube.srv.gen.attendance.management.server.leavepost.rocketmq.consumer;

import com.guhecloud.cube.base.flow.client.FlowResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.repository.AttApproveLogRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttApproveLogPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.leavepost.commandgateway.LeavePostCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.leavepost.commandgateway.commond.LeavePostCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.stat.commandgateway.AttendanceStatServiceImpl;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import instance.event.ProcessCompletedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 离岗审批通过后，消费补卡信息
 */

@Slf4j
@Component("LeavePost_Approval_Over")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = ProcessCompletedMessage.TOPIC + "_" + "LEAVE-POST", topic = ProcessCompletedMessage.TOPIC,
                         selectorExpression = "LEAVE_THE_POST"
)
public class LeavePostApprovalOverConsumer implements RocketMQListener<ProcessCompletedMessage> {
    private final LeavePostCommandService leavePostCommandService;
    private final AttendanceStatServiceImpl attendanceStatService;
    private final AttApproveLogRepository approveLogRepository;

    @Override
    public void onMessage(ProcessCompletedMessage message) {
        log.info("离岗审批通过：{}", JsonUtils.serialize(message));
        AttApproveLogPO po = new AttApproveLogPO();
        po.setStatus(FlowResult.PASS.name().equals(message.getResult().name()) ? FlowResult.FINISHED.name() : message.getResult().name());
        po.setProcessInstanceId(message.getProcessInstanceId());
        try {
            Boolean bool = approveLogRepository.updateApproveLog(po);
            log.warn("{}:离岗审批修改记录结果：{}：", JsonUtils.serialize(po), bool);
        } catch (Exception e) {
            log.error("离岗审批修改记录失败：", e);
        }
        if (FlowResult.PASS.name().equals(message.getResult().name())) {
            try {
                LeavePostCommand command = JsonUtils.deserialize(message.getData(), LeavePostCommand.class);
                command.setApplyTime(message.getStartTime());
                command.setApplyDoneTime(message.getCompletedTime());
                command.setApplicantId(message.getStartUserId());
                leavePostCommandService.leavePostAndUpdateWorkStatus(command);
                // 更新员工统计数据
                Period period = new Period(DateUtils.beginOfMonth(command.getStartTime()), DateUtils.endOfMonth(command.getEndTime()));
                attendanceStatService.staffStat(period, command.getApplicantId(), message.getTenant());
            } catch (Exception e) {
                log.error("离岗审批通过后，消费报错：{}", JsonUtils.serialize(message), e);
            }
        }
    }
}
