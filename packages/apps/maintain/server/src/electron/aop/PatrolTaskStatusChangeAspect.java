package com.guhecloud.cube.srv.gen.maintain.server.electron.aop;

import com.alibaba.fastjson2.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.guhecloud.cube.biz.ouaa.client.ApplicationClient;
import com.guhecloud.cube.biz.ouaa.client.dto.ApplicationDTO;
import com.guhecloud.cube.biz.todo.client.enumeration.TodoTaskFlowOperationEnum;
import com.guhecloud.cube.biz.todo.client.message.TodoTaskFlowCirculationMessage;
import com.guhecloud.cube.biz.todo.client.message.TodoTaskInitMessage;
import com.guhecloud.cube.biz.work.center.client.enumeration.WorkTaskFlowOperationEnum;
import com.guhecloud.cube.biz.work.center.client.message.WorkTaskFlowCirculationMessage;
import com.guhecloud.cube.biz.work.center.client.message.WorkTaskInitMessage;
import com.guhecloud.cube.srv.gen.maintain.server.common.enumeration.MaintainApplicationEnum;
import com.guhecloud.cube.srv.gen.maintain.server.electron.dao.PatrolTaskMapper;
import com.guhecloud.cube.srv.gen.maintain.server.electron.entity.PatrolTask;
import com.guhecloud.cube.srv.gen.maintain.server.electron.enumeration.PatrolTaskStateEnum;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wql
 * @describe 巡更任务状态变更推送待办
 * @date 2023/6/2 9:29
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PatrolTaskStatusChangeAspect {
    String oldTaskStatus;
    private final PatrolTaskMapper patrolTaskMapper;
    private final ApplicationClient applicationClient;

    @Pointcut("execution(* com.guhecloud.cube.srv.gen.maintain.server.electron.service.PatrolTaskStatusChangeService.*(..))")
    public void point() {
    }

    @Before(value = "point()")
    public void beforeEvent(JoinPoint joinPoint) {
        Long taskId = (Long) joinPoint.getArgs()[0];
        PatrolTask task = patrolTaskMapper.selectById(taskId);
        if (task != null) {
            oldTaskStatus = task.getStatus();
        }
    }

    @AfterReturning(value = "point()", returning = "res")
    public void publishEvent(JoinPoint joinPoint, Object res) {
        try {
            PatrolTask po = (PatrolTask) res;
            //获取应用
            ApplicationDTO application = applicationClient.getApplicationByCode(MaintainApplicationEnum.ELECTRONIC_PATROL.name());
            //发送 待办任务推送
            this.agency(po, application);
        } catch (Exception e) {
            log.error("待办任务推送异常:{}", e);
        }
    }

    private void agency(PatrolTask po, ApplicationDTO application) {
        List<Long> list = Arrays.stream(po.getBelonger().split(",")).map(Long::parseLong).distinct().collect(Collectors.toList());
        // 待接收 任务生成
        if (PatrolTaskStateEnum.TO_BE_RECEIVED.name().equals(po.getStatus())) {
            //创建消息体
            TodoTaskInitMessage message = new TodoTaskInitMessage();
            message.setApplicationCode(application.getCode());
            message.setApplicationName(application.getName());
            message.setBusinessKey(String.valueOf(po.getId()));
            message.setBusinessType(MaintainApplicationEnum.ELECTRONIC_PATROL.name());
            message.setBusinessTypeName(MaintainApplicationEnum.ELECTRONIC_PATROL.getDescription());
            message.setBusinessStatus(po.getStatus());
            message.setBusinessStatusName(PatrolTaskStateEnum.valueOf(po.getStatus()).getRemarks());
            message.setTaskName(po.getTaskName());
            message.setTaskTime(po.getCreateTime());
            message.setOperationType(TodoTaskFlowOperationEnum.INSERT);
            message.setUserIds(list);
            //发消息
            RocketMQContextHolder.syncSendOrderly(TodoTaskInitMessage.TOPIC, message, String.valueOf(po.getId()));
            log.info("发送巡更任务待办消息:{}", JSON.toJSONString(message));
            //创建消息体
            sendWorkInitMsg(po, application, list);
            log.info("发送巡更工作中心消息:{}", JSON.toJSONString(message));
        } else {
            Long userId = po.getUserId();

            TodoTaskFlowCirculationMessage message = new TodoTaskFlowCirculationMessage();
            message.setBusinessKey(String.valueOf(po.getId()));
            message.setBusinessStatus(oldTaskStatus);
            message.setUserIds(userId == null ? Sets.newHashSet(list) : Sets.newHashSet(userId));
            message.setHandlerTime(LocalDateTime.now());
            message.setTaskTime(LocalDateTime.now());
            message.setGeneratedNextStatusTask(!(PatrolTaskStateEnum.FINISHED.name().equals(po.getStatus())));
            message.setOperationType(TodoTaskFlowOperationEnum.CHANGE);
            message.setNextBusinessStatus(po.getStatus());
            message.setNextBusinessStatusName(PatrolTaskStateEnum.valueOf(po.getStatus()).getRemarks());
            message.setNextUserIds(userId == null ? list : Lists.newArrayList(userId));
            //发消息
            RocketMQContextHolder.syncSendOrderly(TodoTaskFlowCirculationMessage.TOPIC, message, String.valueOf(po.getId()));
            log.info("巡更任务待办消息更新:{}", JSON.toJSONString(message));
            sendWorkTaskFlowCirculationMsg(po, list, userId);
            log.info("巡更任务工作中心消息更新:{}", JSON.toJSONString(message));
        }

    }

    private void sendWorkInitMsg(PatrolTask po, ApplicationDTO application, List<Long> list) {
        WorkTaskInitMessage message = new WorkTaskInitMessage();
        message.setApplicationCode(application.getCode());
        message.setApplicationName(application.getName());
        message.setBusinessKey(String.valueOf(po.getId()));
        message.setBusinessType(MaintainApplicationEnum.ELECTRONIC_PATROL.name());
        message.setBusinessTypeName(MaintainApplicationEnum.ELECTRONIC_PATROL.getDescription());
        message.setBusinessStatus(po.getStatus());
        message.setBusinessStatusName(PatrolTaskStateEnum.valueOf(po.getStatus()).getRemarks());
        message.setTaskName(po.getTaskName());
        message.setTaskTime(po.getCreateTime());
        message.setOperationType(WorkTaskFlowOperationEnum.INSERT);
        message.setUserIds(list);
        //发消息
        RocketMQContextHolder.syncSendOrderly(WorkTaskInitMessage.TOPIC, message, String.valueOf(po.getId()));
    }

    private void sendWorkTaskFlowCirculationMsg(PatrolTask po, List<Long> list, Long userId) {
        WorkTaskFlowCirculationMessage message = new WorkTaskFlowCirculationMessage();
        message.setBusinessKey(String.valueOf(po.getId()));
        message.setBusinessStatus(oldTaskStatus);
        message.setUserIds(userId == null ? Sets.newHashSet(list) : Sets.newHashSet(userId));
        message.setHandlerTime(LocalDateTime.now());
        message.setTaskTime(LocalDateTime.now());
        message.setGeneratedNextStatusTask(!(PatrolTaskStateEnum.FINISHED.name().equals(po.getStatus())));
        message.setOperationType(WorkTaskFlowOperationEnum.CHANGE);
        message.setNextBusinessStatus(po.getStatus());
        message.setNextBusinessStatusName(PatrolTaskStateEnum.valueOf(po.getStatus()).getRemarks());
        message.setNextUserIds(userId == null ? list : Lists.newArrayList(userId));
        //发消息
        RocketMQContextHolder.syncSendOrderly(WorkTaskFlowCirculationMessage.TOPIC, message, String.valueOf(po.getId()));
    }
}
