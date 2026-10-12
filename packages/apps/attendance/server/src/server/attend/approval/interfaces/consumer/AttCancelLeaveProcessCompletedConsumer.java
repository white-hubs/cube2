package com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.interfaces.consumer;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.flow.client.FlowResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.commandgateway.AttCancelLeaveCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.dto.AttCancelLeaveFromDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.mapper.AttCancelLeaveFromMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.po.AttCancelLeaveFromPO;
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
 * 流程完成监听事件（销假审批）
 * 销假审批通过或拒绝
 */
@Slf4j
@Component
@RocketMQMessageListener(
        consumerGroup = ProcessCompletedMessage.TOPIC + "_ATT_CANCEL_LEAVE",
        topic = ProcessCompletedMessage.TOPIC,
        selectorExpression = "CANCEL_LEAVE",
        selectorType = SelectorType.TAG
)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttCancelLeaveProcessCompletedConsumer implements RocketMQListener<ProcessCompletedMessage> {

    private final AttApproveLogRepository approveLogRepository;
    private final AttCancelLeaveCommandService attCancelLeaveCommandService;
    private final AttCancelLeaveFromMapper attCancelLeaveFromMapper;

    @Override
    public void onMessage(ProcessCompletedMessage processCompletedMessage) {
        log.info("收到销假流程结束：{}", JsonUtils.serialize(processCompletedMessage));
        log.info("操作状态：{}", processCompletedMessage.getResult());
        AttCancelLeaveFromDTO fromDTO = JsonUtils.deserialize(processCompletedMessage.getData(), AttCancelLeaveFromDTO.class);
        AttCancelLeaveFromPO fromPO = attCancelLeaveFromMapper.selectOne(Wrappers.<AttCancelLeaveFromPO>lambdaQuery()
                                                                                 .eq(AttCancelLeaveFromPO::getProcessInstanceId, processCompletedMessage.getProcessInstanceId())
                                                                                 .last(" limit 1 ")
                                                                        );
        if (fromPO != null) {
            BeanUtils.copyProperties(fromPO, fromDTO);
        }
        // 扣减或回滚假期余额
        try {
            if (processCompletedMessage.getResult() == FlowResult.PASS) {
                attCancelLeaveCommandService.cancelLeave(fromDTO);
                log.info("销假审批通过扣减余额成功！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("{}销假流程结束,余额台账消费失败：{}", processCompletedMessage.getResult(), JsonUtils.serialize(fromDTO));
        }
        // 更新销假单状态
        attCancelLeaveFromMapper.update(null, Wrappers.<AttCancelLeaveFromPO>lambdaUpdate()
                                                .eq(AttCancelLeaveFromPO::getProcessInstanceId, processCompletedMessage.getProcessInstanceId())
                                                .set(AttCancelLeaveFromPO::getStatus,
                                                     FlowResult.PASS.name().equals(processCompletedMessage.getResult().name()) ? FlowResult.FINISHED.name()
                                                                                                                               : processCompletedMessage.getResult().name())
                                                .set(AttCancelLeaveFromPO::getUpdateTime, LocalDateTime.now())
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
