package com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.interfaces.consumer;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.flow.client.FlowResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.mapper.AttAskLeaveFromMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.mapper.AttAwayBusinessFromMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.mapper.AttLeavePostFromMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.po.AttAskLeaveFromPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.po.AttAwayBusinessFromPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.po.AttLeavePostFromPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.mapper.AttApproveLogMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttApproveLogPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.dao.WorkOvertimeLedgerAdvanceMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.entity.WorkOvertimeLedgerAdvance;
import com.guhecloud.cube.srv.gen.attendance.management.server.common.ClassesStatusTriggerEnum;
import instance.event.ProcessWithdrawMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 监听请申请节点回退
 */
@Slf4j
@Component
@RocketMQMessageListener(
        consumerGroup = "FLOW_WITHDRAW_ATT_WITHDRAW",
        topic = "FLOW_WITHDRAW"
)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttApprovalWithdrawConsumer implements RocketMQListener<ProcessWithdrawMessage> {

    private static final int NODE_NUMBER = 3;
    private final AttApproveLogMapper attApproveLogMapper;
    private final AttAskLeaveFromMapper attAskLeaveFromMapper;
    private final AttLeavePostFromMapper attLeavePostFromMapper;
    private final AttAwayBusinessFromMapper attAwayBusinessFromMapper;
    private final WorkOvertimeLedgerAdvanceMapper workOvertimeLedgerAdvanceMapper;

    @Override
    public void onMessage(ProcessWithdrawMessage withdrawMessage) {

        if (NODE_NUMBER != withdrawMessage.getProcessNodeNum()) {
            return;
        }
        String status = FlowResult.ACTIVE.name();
        LocalDateTime newTime = LocalDateTime.now();
        if (ClassesStatusTriggerEnum.LEAVE.name().equals(withdrawMessage.getBusinessType())) {
            // 请假
            attAskLeaveFromMapper.update(null, Wrappers.<AttAskLeaveFromPO>lambdaUpdate()
                                                 .set(AttAskLeaveFromPO::getStatus, status)
                                                 .set(AttAskLeaveFromPO::getUpdateTime, newTime)
                                                 .eq(AttAskLeaveFromPO::getProcessInstanceId, withdrawMessage.getProcessInstanceId())
                                        );
        } else if (ClassesStatusTriggerEnum.LEAVE_THE_POST.name().equals(withdrawMessage.getBusinessType())) {
            // 离岗
            attLeavePostFromMapper.update(null, Wrappers.<AttLeavePostFromPO>lambdaUpdate()
                                                  .set(AttLeavePostFromPO::getStatus, status)
                                                  .set(AttLeavePostFromPO::getUpdateTime, newTime)
                                                  .eq(AttLeavePostFromPO::getProcessInstanceId, withdrawMessage.getProcessInstanceId())
                                         );
        } else if (ClassesStatusTriggerEnum.AWAY_BUSINESS.name().equals(withdrawMessage.getBusinessType())) {
            // 出差
            attAwayBusinessFromMapper.update(null, Wrappers.<AttAwayBusinessFromPO>lambdaUpdate()
                                                     .set(AttAwayBusinessFromPO::getStatus, status)
                                                     .set(AttAwayBusinessFromPO::getUpdateTime, newTime)
                                                     .eq(AttAwayBusinessFromPO::getProcessInstanceId, withdrawMessage.getProcessInstanceId())
                                            );
        } else if (ClassesStatusTriggerEnum.OVERTIME.name().equals(withdrawMessage.getBusinessType())) {
            // 加班
            workOvertimeLedgerAdvanceMapper.delete(Wrappers.<WorkOvertimeLedgerAdvance>lambdaQuery()
                                                           .eq(WorkOvertimeLedgerAdvance::getProcessInstanceId, withdrawMessage.getProcessInstanceId())
                                                  );
        }

        // 更新审批日志表状态
        String logStatus = "PROGRESS";
        attApproveLogMapper.update(null, Wrappers.<AttApproveLogPO>lambdaUpdate()
                                           .eq(AttApproveLogPO::getProcessInstanceId, withdrawMessage.getProcessInstanceId())
                                           .set(AttApproveLogPO::getStatus, logStatus)
                                           .set(AttApproveLogPO::getUpdateTime, LocalDateTime.now())
                                  );
    }
}
