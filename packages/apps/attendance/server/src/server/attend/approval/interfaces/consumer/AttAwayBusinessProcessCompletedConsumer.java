package com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.interfaces.consumer;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.flow.client.FlowResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.commandgateway.AttAwayBusinessLedgerCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.dto.AttAwayBusinessFromDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.mapper.AttAwayBusinessFromMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.po.AttAwayBusinessFromPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.repository.AttApproveLogRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttApproveLogPO;
import com.guhecloud.platform.base.util.JsonUtils;
import instance.event.ProcessCompletedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 出差审批通过/流程结束
 */
@Slf4j
@Component("AwayBusiness_Approval_Over")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(
        consumerGroup = ProcessCompletedMessage.TOPIC + "_ATT_AWAY_BUSINESS"
        , topic = ProcessCompletedMessage.TOPIC
        , selectorExpression = "AWAY_BUSINESS"
)
public class AttAwayBusinessProcessCompletedConsumer implements RocketMQListener<ProcessCompletedMessage> {

    private final AttAwayBusinessFromMapper attAwayBusinessFromMapper;
    private final AttApproveLogRepository attApproveLogRepository;
    private final AttAwayBusinessLedgerCommandService attAwayBusinessLedgerCommandService;

    @Override
    public void onMessage(ProcessCompletedMessage processCompletedMessage) {
        log.info("收到出差流程结束：{}", JsonUtils.serialize(processCompletedMessage));
        log.info("操作状态：{}", processCompletedMessage.getResult());
        AttAwayBusinessFromDTO fromDTO = JsonUtils.deserialize(processCompletedMessage.getData(), AttAwayBusinessFromDTO.class);
        AttAwayBusinessFromPO fromPO = attAwayBusinessFromMapper.selectOne(Wrappers.<AttAwayBusinessFromPO>lambdaQuery()
                                                                                   .eq(AttAwayBusinessFromPO::getProcessInstanceId, processCompletedMessage.getProcessInstanceId())
                                                                                   .last(" limit 1 ")
                                                                          );
        if (fromPO != null) {
            BeanUtils.copyProperties(fromPO, fromDTO);
        }
        fromDTO.setProcessInstanceId(processCompletedMessage.getProcessInstanceId());
        if (processCompletedMessage.getResult() == FlowResult.PASS) {
            try {
                attAwayBusinessLedgerCommandService.addAwayBusinessLedger(fromDTO);
                log.info("请出差审批通过写入出差台账成功！");
            } catch (Exception e) {
                log.error("{}出差流程结束,余额台账消费失败：{}", processCompletedMessage.getResult(), JsonUtils.serialize(fromDTO));
                e.printStackTrace();
            }
        }
        // 更新出差单状态
        attAwayBusinessFromMapper.update(null, Wrappers.<AttAwayBusinessFromPO>lambdaUpdate()
                                                 .eq(AttAwayBusinessFromPO::getProcessInstanceId, processCompletedMessage.getProcessInstanceId())
                                                 .set(AttAwayBusinessFromPO::getStatus,
                                                      FlowResult.PASS.name().equals(processCompletedMessage.getResult().name()) ? FlowResult.FINISHED.name()
                                                                                                                                : processCompletedMessage.getResult().name())
                                                 .set(AttAwayBusinessFromPO::getUpdateTime, LocalDateTime.now())
                                        );
        // 更新审批日志表状态
        AttApproveLogPO attApproveLog = new AttApproveLogPO();
        attApproveLog.setProcessInstanceId(processCompletedMessage.getProcessInstanceId());
        attApproveLog.setStatus(FlowResult.PASS.name().equals(processCompletedMessage.getResult().name()) ? FlowResult.FINISHED.name()
                                                                                                          : processCompletedMessage
                                        .getResult().name());
        attApproveLogRepository.updateApproveLog(attApproveLog);
    }

}
