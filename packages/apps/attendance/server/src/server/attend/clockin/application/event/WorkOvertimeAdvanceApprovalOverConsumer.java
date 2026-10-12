package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.application.event;

import com.guhecloud.platform.base.util.ObjectUtils;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.repository.AttApproveLogRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttApproveLogPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.dao.WorkOvertimeLedgerAdvanceMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.entity.WorkOvertimeLedgerAdvance;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.param.WorkOvertimeSituationParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.server.WorkOvertimeLedgerAdvanceServer;
import instance.event.NodePassMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 加班审批节点通过后，消费加班信息
 */

@Slf4j
@Component("Overtime_Advance_Approval_Over")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = NodePassMessage.TOPIC + "_OVERTIME", topic = NodePassMessage.TOPIC,
                         selectorExpression = "OVERTIME"
)
public class WorkOvertimeAdvanceApprovalOverConsumer implements RocketMQListener<NodePassMessage> {
    private final AttApproveLogRepository approveLogRepository;
    private final WorkOvertimeLedgerAdvanceMapper workOvertimeLedgerAdvanceMapper;
    private final WorkOvertimeLedgerAdvanceServer workOvertimeLedgerAdvanceServer;

    @Override
    public void onMessage(NodePassMessage message) {
        log.info("加班审批节点通过：{}", JSONObject.toJSONString(message));
        List<AttApproveLogPO> attApproveLog = approveLogRepository.getApproveLog(message.getProcessInstanceId());
        if (ObjectUtils.isEmpty(attApproveLog)) {
            log.info("未找到审批信息：{}", message.getProcessInstanceId());
            return;
        }
        try {
            WorkOvertimeLedgerAdvance workOvertimeLedgerAdvance = workOvertimeLedgerAdvanceMapper.selectOne(new LambdaQueryWrapper<WorkOvertimeLedgerAdvance>()
                                                                                                                    .eq(WorkOvertimeLedgerAdvance::getProcessInstanceId, message.getProcessInstanceId())
                                                                                                                    .last("limit 1"));
            if (ObjectUtils.isEmpty(workOvertimeLedgerAdvance)) {
                //参数校验和填充数据
                WorkOvertimeSituationParam command = WorkOvertimeSituationParam.getWorkOvertimeSituationParam(message.getData());
                command.setApplyTime(message.getStartTime());
                workOvertimeLedgerAdvanceServer.addWorkOvertime(command, attApproveLog);
            }
        } catch (Exception e) {
            log.error("加班审批节点通过后,消费报错：", e);
        }
    }
}
