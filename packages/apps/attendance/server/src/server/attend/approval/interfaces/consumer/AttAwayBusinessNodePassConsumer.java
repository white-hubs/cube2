package com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.interfaces.consumer;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.flow.client.FlowResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.mapper.AttAwayBusinessFromMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.po.AttAwayBusinessFromPO;
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
 * 监听出差节点通过（出差审批首节点通过）
 * 通过或拒绝
 */
@Slf4j
@Component
@RocketMQMessageListener(
        consumerGroup = NodePassMessage.TOPIC + "_ATT_AWAY_BUSINESS",
        topic = NodePassMessage.TOPIC,
        selectorExpression = "AWAY_BUSINESS",
        selectorType = SelectorType.TAG
)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttAwayBusinessNodePassConsumer implements RocketMQListener<NodePassMessage> {

    private final AttApproveLogMapper attApproveLogMapper;
    private final AttAwayBusinessFromMapper attAwayBusinessFromMapper;

    @Override
    public void onMessage(NodePassMessage nodePassMessage) {
        log.info("收到出差节点通过：{}", JsonUtils.serialize(nodePassMessage));

        // 更新审批日志表状态
        attApproveLogMapper.update(null, Wrappers.<AttApproveLogPO>lambdaUpdate()
                                           .eq(AttApproveLogPO::getProcessInstanceId, nodePassMessage.getProcessInstanceId())
                                           .eq(AttApproveLogPO::getStatus, FlowResult.ACTIVE.name())
                                           .set(AttApproveLogPO::getStatus, FlowResult.PASS.name())
                                           .set(AttApproveLogPO::getUpdateTime, LocalDateTime.now())
                                  );

        // 更新出差单状态
        attAwayBusinessFromMapper.update(null, Wrappers.<AttAwayBusinessFromPO>lambdaUpdate()
                                                 .eq(AttAwayBusinessFromPO::getProcessInstanceId, nodePassMessage.getProcessInstanceId())
                                                 .eq(AttAwayBusinessFromPO::getStatus, FlowResult.ACTIVE.name())
                                                 .set(AttAwayBusinessFromPO::getStatus, FlowResult.PASS.name())
                                                 .set(AttAwayBusinessFromPO::getUpdateTime, LocalDateTime.now())
                                        );
    }
}
