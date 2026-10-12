package com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.interfaces.consumer;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.flow.client.FlowResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.mapper.AttApproveLogMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttApproveLogPO;
import com.guhecloud.platform.base.util.JsonUtils;
import instance.event.NodePassMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 监听补卡节点通过（补卡审批首节点通过）
 * 通过或拒绝
 */
@Slf4j
@Component
@RocketMQMessageListener(
        consumerGroup = NodePassMessage.TOPIC + "_ATT_REPAIR_CLOCK",
        topic = NodePassMessage.TOPIC,
        selectorExpression = "REPAIR_CLOCK",
        selectorType = SelectorType.TAG
)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttRepairClockNodePassConsumer implements RocketMQListener<NodePassMessage> {

    private final AttApproveLogMapper attApproveLogMapper;

    @Override
    public void onMessage(NodePassMessage nodePassMessage) {
        log.info("收到补卡节点通过：{}", JsonUtils.serialize(nodePassMessage));

        // 更新审批日志表状态
        attApproveLogMapper.update(null, Wrappers.<AttApproveLogPO>lambdaUpdate()
                                           .eq(AttApproveLogPO::getProcessInstanceId, nodePassMessage.getProcessInstanceId())
                                           .eq(AttApproveLogPO::getStatus, FlowResult.ACTIVE.name())
                                           .set(AttApproveLogPO::getStatus, FlowResult.PASS.name())
                                           .set(AttApproveLogPO::getUpdateTime, LocalDateTime.now())
                                  );
    }
}
