package com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.interfaces.consumer;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.flow.client.FlowResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.beforestat.application.dto.AttNodeChangeClassDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.mapper.AttApproveLogMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttApproveLogPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.mapper.AttClassesMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttClassesPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.changeclasses.commandgateway.command.ClassesChangeCommand;
import com.guhecloud.platform.base.util.DateUtils;
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
        consumerGroup = NodePassMessage.TOPIC + "_ATT_CHANGE_CLASSES",
        topic = NodePassMessage.TOPIC,
        selectorExpression = "CHANGE_CLASSES",
        selectorType = SelectorType.TAG
)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttChangeClassNodePassConsumer implements RocketMQListener<NodePassMessage> {

    private final AttClassesMapper attClassesMapper;
    private final AttApproveLogMapper attApproveLogMapper;

    @Override
    public void onMessage(NodePassMessage nodePassMessage) {
        log.info("收到换班节点通过：{}", JsonUtils.serialize(nodePassMessage));
        ClassesChangeCommand command = JsonUtils.deserialize(nodePassMessage.getData(), ClassesChangeCommand.class);
        // 更新审批日志表状态
        attApproveLogMapper.update(null, Wrappers.<AttApproveLogPO>lambdaUpdate()
                                           .eq(AttApproveLogPO::getProcessInstanceId, nodePassMessage.getProcessInstanceId())
                                           .eq(AttApproveLogPO::getStatus, FlowResult.ACTIVE.name())
                                           .set(AttApproveLogPO::getStatus, FlowResult.PASS.name())
                                           .set(AttApproveLogPO::getUpdateTime, LocalDateTime.now())
                                  );
        AttClassesPO fromClassPO = new AttClassesPO();
        AttClassesPO toClassPO = new AttClassesPO();
        if (command.getFromClassesId() != null) {
            fromClassPO = attClassesMapper.selectById(command.getFromClassesId());
        }
        if (command.getToClassesId() != null) {
            if (!command.getToClassesId().equals(command.getFromClassesId())) {
                toClassPO = attClassesMapper.selectById(command.getToClassesId());
            } else {
                toClassPO = fromClassPO;
            }
        }
        if (fromClassPO == null) {
            fromClassPO = new AttClassesPO();
        }
        if (toClassPO == null) {
            toClassPO = new AttClassesPO();
        }
        try {
            AttNodeChangeClassDTO nodeDTO = new AttNodeChangeClassDTO();
            nodeDTO.setId(IdWorker.getId());
            nodeDTO.setProcessInstanceId(nodePassMessage.getProcessInstanceId());

            nodeDTO.setFromId(command.getApplicantId());
            nodeDTO.setFromDate(command.getFromDate());
            nodeDTO.setFromClassId(command.getFromClassesId());
            nodeDTO.setFromClassType(fromClassPO.getClassesType());
            if (command.getFromDate() != null && fromClassPO.getStartWorkTime() != null && fromClassPO.getEndWorkTime() != null) {
                String fromDateStr = DateUtils.formatDate(command.getFromDate());
                nodeDTO.setFromClassStartTime(DateUtils.parse(fromDateStr + " " + fromClassPO.getStartWorkTime() + ":00"));
                nodeDTO.setFromClassEndTime(DateUtils.parse(fromDateStr + " " + fromClassPO.getEndWorkTime() + ":00"));
            }
            if (command.getToDate() != null && toClassPO.getStartWorkTime() != null && toClassPO.getEndWorkTime() != null) {
                String toDateStr = DateUtils.formatDate(command.getToDate());
                nodeDTO.setToClassStartTime(DateUtils.parse(toDateStr + " " + toClassPO.getStartWorkTime() + ":00"));
                nodeDTO.setToClassEndTime(DateUtils.parse(toDateStr + " " + toClassPO.getEndWorkTime() + ":00"));
            }
            nodeDTO.setToId(command.getChangerId());
            nodeDTO.setToDate(command.getToDate());
            nodeDTO.setToClassId(command.getToClassesId());
            nodeDTO.setToClassType(toClassPO.getClassesType());
            String str = JsonUtils.serialize(nodeDTO);
            attApproveLogMapper.update(null, Wrappers.<AttApproveLogPO>lambdaUpdate()
                                               .eq(AttApproveLogPO::getProcessInstanceId, nodePassMessage.getProcessInstanceId())
                                               .set(AttApproveLogPO::getProportion, str)
                                               .set(AttApproveLogPO::getUpdateTime, LocalDateTime.now())
                                      );
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
