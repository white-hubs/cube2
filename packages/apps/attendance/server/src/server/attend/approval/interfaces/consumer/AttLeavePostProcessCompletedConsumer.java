package com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.interfaces.consumer;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.flow.client.FlowResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.commandgateway.AttLeavePostLedgerCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.dto.AttLeavePostFromDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.mapper.AttLeavePostFromMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.po.AttLeavePostFromPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.repository.AttApproveLogRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttApproveLogPO;
import com.guhecloud.platform.base.util.JsonUtils;
import instance.event.ProcessCompletedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 流程完成监听事件（监听离岗审批监听）
 * 通过或拒绝
 */
@Slf4j
@Component
@RocketMQMessageListener(
        consumerGroup = ProcessCompletedMessage.TOPIC + "_ATT_LEAVE_POST",
        topic = ProcessCompletedMessage.TOPIC,
        selectorExpression = "LEAVE_THE_POST",
        selectorType = SelectorType.TAG
)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttLeavePostProcessCompletedConsumer implements RocketMQListener<ProcessCompletedMessage> {

    private final AttApproveLogRepository approveLogRepository;
    private final AttLeavePostFromMapper attLeavePostFromMapper;
    private final AttLeavePostLedgerCommandService attLeavePostLedgerCommandService;

    @Override
    public void onMessage(ProcessCompletedMessage processCompletedMessage) {
        log.info("收到离岗流程结束：{}", JsonUtils.serialize(processCompletedMessage));
        log.info("操作状态：{}", processCompletedMessage.getResult());
        AttLeavePostFromDTO fromDTO = JsonUtils.deserialize(processCompletedMessage.getData(), AttLeavePostFromDTO.class);
        AttLeavePostFromPO fromPO = attLeavePostFromMapper.selectOne(Wrappers.<AttLeavePostFromPO>lambdaQuery()
                                                                             .eq(AttLeavePostFromPO::getProcessInstanceId, processCompletedMessage.getProcessInstanceId())
                                                                             .last(" limit 1 ")
                                                                    );
        if (fromPO != null) {
            BeanUtils.copyProperties(fromPO, fromDTO);
        }
        fromDTO.setProcessInstanceId(processCompletedMessage.getProcessInstanceId());
        if (processCompletedMessage.getResult() == FlowResult.PASS) {
            try {
                attLeavePostLedgerCommandService.addLeavePostLedger(fromDTO);
                log.info("请离岗审批通过写入离岗台账成功！");
            } catch (Exception e) {
                log.error("{}离岗流程结束,余额台账消费失败：{}", processCompletedMessage.getResult(), JsonUtils.serialize(fromDTO));
                e.printStackTrace();
            }
        }
        // 更新离岗单状态
        attLeavePostFromMapper.update(null, Wrappers.<AttLeavePostFromPO>lambdaUpdate()
                                              .eq(AttLeavePostFromPO::getProcessInstanceId, processCompletedMessage.getProcessInstanceId())
                                              .set(AttLeavePostFromPO::getStatus,
                                                   FlowResult.PASS.name().equals(processCompletedMessage.getResult().name()) ? FlowResult.FINISHED.name()
                                                                                                                             : processCompletedMessage.getResult().name())
                                              .set(AttLeavePostFromPO::getUpdateTime, LocalDateTime.now())
                                     );
        // 更新审批日志表状态
        AttApproveLogPO attApproveLog = new AttApproveLogPO();
        attApproveLog.setProcessInstanceId(processCompletedMessage.getProcessInstanceId());
        attApproveLog.setStatus(FlowResult.PASS.name().equals(processCompletedMessage.getResult().name()) ? FlowResult.FINISHED.name()
                                                                                                          : processCompletedMessage
                                        .getResult().name());
        approveLogRepository.updateApproveLog(attApproveLog);
    }
}
