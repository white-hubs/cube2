package com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.interfaces.consumer;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.flow.client.FlowResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.mapper.AttAskLeaveFromMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.po.AttAskLeaveFromPO;
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
 * 监听请假节点通过（请假首节点通过）
 * 通过或拒绝
 */
@Slf4j
@Component
@RocketMQMessageListener(
        consumerGroup = NodePassMessage.TOPIC + "_ATT_LEAVE",
        topic = NodePassMessage.TOPIC,
        selectorExpression = "LEAVE",
        selectorType = SelectorType.TAG
)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttAskLeaveNodePassConsumer implements RocketMQListener<NodePassMessage> {

    private final AttApproveLogMapper attApproveLogMapper;
    private final AttAskLeaveFromMapper attAskLeaveFromMapper;

    @Override
    public void onMessage(NodePassMessage nodePassMessage) {
        log.info("收到请假节点通过：{}", JsonUtils.serialize(nodePassMessage));

        // 更新审批日志表状态
        attApproveLogMapper.update(null, Wrappers.<AttApproveLogPO>lambdaUpdate()
                                           .eq(AttApproveLogPO::getProcessInstanceId, nodePassMessage.getProcessInstanceId())
                                           .eq(AttApproveLogPO::getStatus, FlowResult.ACTIVE.name())
                                           .set(AttApproveLogPO::getStatus, FlowResult.PASS.name())
                                           .set(AttApproveLogPO::getUpdateTime, LocalDateTime.now())
                                  );

        // 更新请假单状态
        attAskLeaveFromMapper.update(null, Wrappers.<AttAskLeaveFromPO>lambdaUpdate()
                                             .eq(AttAskLeaveFromPO::getProcessInstanceId, nodePassMessage.getProcessInstanceId())
                                             .eq(AttAskLeaveFromPO::getStatus, FlowResult.ACTIVE.name())
                                             .set(AttAskLeaveFromPO::getStatus, FlowResult.PASS.name())
                                             .set(AttAskLeaveFromPO::getUpdateTime, LocalDateTime.now())
                                    );
    }
}
