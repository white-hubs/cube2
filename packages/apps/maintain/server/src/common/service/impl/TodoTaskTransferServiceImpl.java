package com.guhecloud.cube.srv.gen.maintain.server.common.service.impl;

import com.guhecloud.cube.biz.todo.client.enumeration.TodoTaskFlowOperationEnum;
import com.guhecloud.cube.biz.todo.client.message.TodoTaskFlowCirculationMessage;
import com.guhecloud.cube.biz.work.center.client.enumeration.WorkTaskFlowOperationEnum;
import com.guhecloud.cube.biz.work.center.client.message.WorkTaskFlowCirculationMessage;
import com.guhecloud.cube.srv.gen.maintain.server.common.service.TodoTaskTransferService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectTaskStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectTaskPO;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author wql
 * @describe 待办任务转交
 * @date 2023/12/27 17:52
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TodoTaskTransferServiceImpl implements TodoTaskTransferService {

    /**
     * 巡检任务待办转交
     *
     * @param receiveIds
     * @param po         巡检任务
     */
    @Override
    public void transferTodoTask(String receiveIds, InspectTaskPO po) {
        Set<Long> sourceUserId = StringUtils.isNoneBlank(receiveIds) ? Arrays.stream(po.getReceiveIds().split(",")).map(Long::valueOf)
                .collect(Collectors.toSet()) : null;
        TodoTaskFlowCirculationMessage message = new TodoTaskFlowCirculationMessage();
        message.setBusinessKey(String.valueOf(po.getId()));
        message.setBusinessStatus(po.getStatus().name());
        message.setUserIds(null);
        message.setHandlerTime(LocalDateTime.now());
        message.setTaskTime(LocalDateTime.now());
        message.setGeneratedNextStatusTask(!(InspectTaskStatusEnum.OVER.equals(po.getStatus())
                                             || InspectTaskStatusEnum.STOP.equals(po.getStatus())
                                             || InspectTaskStatusEnum.EXPIRE.equals(po.getStatus())));
        message.setOperationType(TodoTaskFlowOperationEnum.CHANGE);
        message.setNextBusinessStatus(po.getStatus().name());
        message.setNextBusinessStatusName(po.getStatus().getDescription());
        message.setNextUserIds(Arrays.stream(po.getReceiveIds().split(",")).map(Long::valueOf).collect(Collectors.toList()));
        //发消息
        RocketMQContextHolder.syncSendOrderly(TodoTaskFlowCirculationMessage.TOPIC, message, String.valueOf(po.getId()));

        WorkTaskFlowCirculationMessage workMessage = new WorkTaskFlowCirculationMessage();
        workMessage.setBusinessKey(String.valueOf(po.getId()));
        workMessage.setBusinessStatus(po.getStatus().name());
        workMessage.setUserIds(sourceUserId);
        workMessage.setHandlerTime(LocalDateTime.now());
        workMessage.setTaskTime(LocalDateTime.now());
        workMessage.setGeneratedNextStatusTask(!(InspectTaskStatusEnum.OVER.equals(po.getStatus())
                                                 || InspectTaskStatusEnum.STOP.equals(po.getStatus())
                                                 || InspectTaskStatusEnum.EXPIRE.equals(po.getStatus())));
        workMessage.setOperationType(WorkTaskFlowOperationEnum.CHANGE);
        workMessage.setNextBusinessStatus(po.getStatus().name());
        workMessage.setNextBusinessStatusName(po.getStatus().getDescription());
        workMessage.setNextUserIds(Arrays.stream(po.getReceiveIds().split(",")).map(Long::valueOf).collect(Collectors.toList()));
        //发消息
        RocketMQContextHolder.syncSendOrderly(WorkTaskFlowCirculationMessage.TOPIC, workMessage, String.valueOf(po.getId()));
    }
}
