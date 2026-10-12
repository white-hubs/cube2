package com.guhecloud.cube.srv.gen.maintain.server.electron.util;

import com.guhecloud.cube.biz.ouaa.client.ApplicationClient;
import com.guhecloud.cube.biz.ouaa.client.dto.ApplicationDTO;
import com.guhecloud.cube.biz.work.center.client.enumeration.WorkTaskFlowOperationEnum;
import com.guhecloud.cube.biz.work.center.client.message.WorkTaskFlowCirculationMessage;
import com.guhecloud.cube.biz.work.center.client.message.WorkTaskInitMessage;
import com.guhecloud.cube.srv.gen.maintain.server.electron.entity.PatrolTask;
import com.guhecloud.cube.srv.gen.maintain.server.electron.enumeration.PatrolTaskStateEnum;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ElectronTaskWorkCenterUtil {
    private final ApplicationClient applicationClient;
    private static final String applicationCode = "ELECTRONIC_PATROL";

    public void workInsert(PatrolTask task, PatrolTaskStateEnum status) {
        String[] userIds = task.getBelonger().split(",");
        List<Long> list = Arrays.stream(userIds).mapToLong(Long::parseLong).boxed().collect(Collectors.toList());
        this.workInsert(task, list, status);
    }

    public void workInsert(PatrolTask task, List<Long> userIds, PatrolTaskStateEnum status) {
        if (ObjectUtils.isEmpty(userIds)) {
            return;
        }
        log.info("巡更任务新增工作任务：{}", task.getStatus());
        //获取应用
        ApplicationDTO application = applicationClient.getApplicationByCode(applicationCode);
        //创建消息体
        WorkTaskInitMessage message = new WorkTaskInitMessage();
        message.setApplicationCode(application.getCode());
        message.setApplicationName(application.getName());
        message.setBusinessKey(String.valueOf(task.getId()));
        message.setBusinessType("ELECTRON_TASK");
        message.setBusinessTypeName("巡更任务");
        message.setBusinessStatus(status.name());
        message.setBusinessStatusName(status.getRemarks());
        message.setTaskName(task.getTaskName());
        message.setTaskTime(LocalDateTime.now());
        message.setOperationType(WorkTaskFlowOperationEnum.INSERT);
        message.setUserIds(userIds);
        //发消息
        RocketMQContextHolder.syncSendOrderly(WorkTaskInitMessage.TOPIC, message, String.valueOf(task.getId()));

    }

    /**
     * 完成工作任务
     */
    public void workComplete(PatrolTask task, Set<Long> userIds, String status) {
        log.info("电子巡更修改工作任务状态：{}", task.getStatus());
        WorkTaskFlowCirculationMessage message = new WorkTaskFlowCirculationMessage();
        message.setBusinessKey(String.valueOf(task.getId()));
        message.setBusinessStatus(status);
        message.setUserIds(userIds);
        message.setHandlerTime(LocalDateTime.now());
        message.setTaskTime(LocalDateTime.now());
        message.setGeneratedNextStatusTask(false);
        message.setOperationType(WorkTaskFlowOperationEnum.CHANGE);
        message.setNextBusinessStatus(PatrolTaskStateEnum.FINISHED.name());
        message.setNextBusinessStatusName(PatrolTaskStateEnum.FINISHED.getRemarks());
        //发消息
        RocketMQContextHolder.syncSendOrderly(WorkTaskFlowCirculationMessage.TOPIC, message, String.valueOf(task.getId()));
    }

    /**
     * 重新发送工作任务
     */
    public void workRepeat(PatrolTask task, List<Long> userIds) {
        WorkTaskFlowCirculationMessage message = new WorkTaskFlowCirculationMessage();
        message.setOperationType(WorkTaskFlowOperationEnum.DELETE);
        message.setBusinessKey(String.valueOf(task.getId()));
        // 删除
        RocketMQContextHolder.syncSendOrderly(WorkTaskFlowCirculationMessage.TOPIC, message, String.valueOf(task.getId()));
        // 重新发送任务
        this.workInsert(task, userIds, PatrolTaskStateEnum.IN_EXECUTION);
    }
}
