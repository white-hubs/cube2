package com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.interfaces.consumer;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.flow.client.FlowResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.commandgateway.AttAskLeaveLedgerCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.dto.AttAskLeaveFromDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.mapper.AttAskLeaveFromMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.po.AttAskLeaveFromPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.repository.AttApproveLogRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttApproveLogPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.repository.AttDefaultHolidayBalanceRepository;
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
 * 流程完成监听事件（监听请假审批监听）
 * 通过或拒绝
 */
@Slf4j
@Component
@RocketMQMessageListener(
        consumerGroup = ProcessCompletedMessage.TOPIC + "_ATT_ASK_LEAVE",
        topic = ProcessCompletedMessage.TOPIC,
        selectorExpression = "LEAVE",
        selectorType = SelectorType.TAG
)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttAskLeaveProcessCompletedConsumer implements RocketMQListener<ProcessCompletedMessage> {

    private final AttApproveLogRepository approveLogRepository;
    private final AttDefaultHolidayBalanceRepository attDefaultHolidayBalanceRepository;
    private final AttAskLeaveFromMapper attAskLeaveFromMapper;
    private final AttAskLeaveLedgerCommandService attAskLeaveLedgerCommandService;

    @Override
    public void onMessage(ProcessCompletedMessage processCompletedMessage) {
        log.info("收到请假流程结束：{}", JsonUtils.serialize(processCompletedMessage));
        log.info("操作状态：{}", processCompletedMessage.getResult());
        AttAskLeaveFromDTO fromDTO = JsonUtils.deserialize(processCompletedMessage.getData(), AttAskLeaveFromDTO.class);
        AttAskLeaveFromPO fromPO = attAskLeaveFromMapper.selectOne(Wrappers.<AttAskLeaveFromPO>lambdaQuery()
                                                                           .eq(AttAskLeaveFromPO::getProcessInstanceId, processCompletedMessage.getProcessInstanceId())
                                                                           .last(" limit 1 ")
                                                                  );
        if (fromPO != null) {
            BeanUtils.copyProperties(fromPO, fromDTO);
        }
        fromDTO.setProcessInstanceId(processCompletedMessage.getProcessInstanceId());
        // 扣减或回滚假期余额
        try {
            if (processCompletedMessage.getResult() == FlowResult.PASS) {
                attDefaultHolidayBalanceRepository.deductOccupyBalance(fromDTO);
                log.info("请假审批通过扣减余额成功！");
                attAskLeaveLedgerCommandService.addAskLeaveLedger(fromDTO);
                log.info("请假审批通过写入请假台账成功！");
            } else if (processCompletedMessage.getResult() == FlowResult.REJECT || processCompletedMessage.getResult() == FlowResult.UNDO) {
                attDefaultHolidayBalanceRepository.rollbackOccupyBalance(fromDTO);
                log.info("请假审批驳回/撤销回滚余额成功！");
            }
        } catch (Exception e) {
            log.error(processCompletedMessage.getResult() + "请假流程结束,余额台账消费失败：{}", JsonUtils.serialize(fromDTO));
        }
        // 更新请假单状态
        attAskLeaveFromMapper.update(null, Wrappers.<AttAskLeaveFromPO>lambdaUpdate()
                                             .eq(AttAskLeaveFromPO::getProcessInstanceId, processCompletedMessage.getProcessInstanceId())
                                             .set(AttAskLeaveFromPO::getStatus,
                                                  FlowResult.PASS.name().equals(processCompletedMessage.getResult().name()) ? FlowResult.FINISHED.name()
                                                                                                                            : processCompletedMessage.getResult().name())
                                             .set(AttAskLeaveFromPO::getUpdateTime, LocalDateTime.now())
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
