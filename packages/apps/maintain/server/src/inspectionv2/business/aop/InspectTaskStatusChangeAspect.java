package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.aop;

import com.guhecloud.cube.biz.ouaa.client.ApplicationClient;
import com.guhecloud.cube.biz.ouaa.client.dto.ApplicationDTO;
import com.guhecloud.cube.biz.todo.client.enumeration.TodoTaskFlowOperationEnum;
import com.guhecloud.cube.biz.todo.client.message.TodoTaskFlowCirculationMessage;
import com.guhecloud.cube.biz.todo.client.message.TodoTaskInitMessage;
import com.guhecloud.cube.biz.work.center.client.enumeration.WorkTaskFlowOperationEnum;
import com.guhecloud.cube.biz.work.center.client.message.WorkTaskFlowCirculationMessage;
import com.guhecloud.cube.biz.work.center.client.message.WorkTaskInitMessage;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.InspectRecordQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectTaskMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectTaskStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectionMappingEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.filter.BusinessTypeInterceptor;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectRecordPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectTaskPO;
import com.guhecloud.platform.base.util.CollectionUtils;
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
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author zwb
 */
@Aspect
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class InspectTaskStatusChangeAspect {

    private final InspectRecordQueryService recordQueryService;

    private final ApplicationClient applicationClient;
    private final InspectTaskMapper taskMapper;

    /**
     * 任务改变前的状态
     */
    private InspectTaskStatusEnum oldTaskStatus;

    @Pointcut("execution(* com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.InspectTaskStatusChangeService.*(..))")
    public void point() {
    }

    @Before(value = "point()")
    public void beforeEvent(JoinPoint joinPoint) {
        Long taskId = (Long) joinPoint.getArgs()[0];
        InspectTaskPO taskPO = taskMapper.selectById(taskId);
        if (taskPO != null) {
            oldTaskStatus = taskPO.getStatus();
        }
    }

    @AfterReturning(value = "point()", returning = "res")
    public void publishEvent(JoinPoint joinPoint, Object res) {
        InspectTaskPO po = (InspectTaskPO) res;
        String businessType = BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.get();
        //获取业务应用映射关系
        InspectionMappingEnum businessTypeEnum = InspectionMappingEnum.getInspectionType(businessType);
        ApplicationDTO application = applicationClient.getApplicationByCode(businessTypeEnum.getApplicationCode());
        //发送 待办任务推送
        this.agency(po, application, businessTypeEnum);

    }

    private void agency(InspectTaskPO po, ApplicationDTO application, InspectionMappingEnum businessTypeEnum) {

        log.info("发送待办消息切面:{}", po);
        // 待处理 任务生成
        if (po.getStatus() == InspectTaskStatusEnum.ACCEPT) {
            //创建消息体
            TodoTaskInitMessage message = new TodoTaskInitMessage();
            message.setApplicationCode(application.getCode());
            message.setApplicationName(application.getName());
            message.setBusinessKey(String.valueOf(po.getId()));
            message.setBusinessType(businessTypeEnum.name());
            message.setBusinessTypeName(businessTypeEnum.getDescription());
            message.setBusinessStatus(po.getStatus().name());
            message.setBusinessStatusName(po.getStatus().getDescription());
            message.setTaskName(po.getName());
            message.setTaskTime(po.getCreateTime());
            message.setOperationType(TodoTaskFlowOperationEnum.INSERT);
            message.setUserIds(Arrays.stream(po.getReceiveIds().split(",")).map(Long::parseLong).distinct().collect(Collectors.toList()));
            RocketMQContextHolder.syncSendOrderly(TodoTaskInitMessage.TOPIC, message, String.valueOf(po.getId()));
            //发送工作中心消息
            WorkTaskInitMessage workTaskInitMessage = new WorkTaskInitMessage();
            workTaskInitMessage.setApplicationCode(application.getCode());
            workTaskInitMessage.setApplicationName(application.getName());
            workTaskInitMessage.setBusinessKey(String.valueOf(po.getId()));
            workTaskInitMessage.setBusinessType(businessTypeEnum.name());
            workTaskInitMessage.setBusinessTypeName(businessTypeEnum.getDescription());
            workTaskInitMessage.setBusinessStatus(po.getStatus().name());
            workTaskInitMessage.setBusinessStatusName(po.getStatus().getDescription());
            workTaskInitMessage.setTaskName(po.getName());
            workTaskInitMessage.setTaskTime(po.getCreateTime());
            workTaskInitMessage.setOperationType(WorkTaskFlowOperationEnum.INSERT);
            workTaskInitMessage.setUserIds(Arrays.stream(po.getReceiveIds().split(",")).map(Long::parseLong).distinct()
                                                   .collect(Collectors.toList()));
            RocketMQContextHolder.syncSendOrderly(WorkTaskInitMessage.TOPIC, workTaskInitMessage, String.valueOf(po.getId()));

        } else {
            Set<Long> userIds = null;
            // 任务完成时 查找完成人
            if (po.getStatus() == InspectTaskStatusEnum.OVER) {
                List<InspectRecordPO> recordPOS = recordQueryService.lambdaQuery()
                        .eq(InspectRecordPO::getTaskId, po.getId())
                        .select(InspectRecordPO::getUserId)
                        .list();

                if (CollectionUtils.isNotEmpty(recordPOS)) {
                    userIds = recordPOS.stream().filter(Objects::nonNull).map(InspectRecordPO::getUserId).collect(Collectors.toSet());
                }
            }

            TodoTaskFlowCirculationMessage message = new TodoTaskFlowCirculationMessage();
            message.setBusinessKey(String.valueOf(po.getId()));
            message.setBusinessStatus(oldTaskStatus.name());
            message.setUserIds(userIds);
            message.setHandlerTime(LocalDateTime.now());
            message.setTaskTime(LocalDateTime.now());
            System.out.println(oldTaskStatus);
            message.setGeneratedNextStatusTask(!(InspectTaskStatusEnum.OVER.equals(po.getStatus())
                                                 || InspectTaskStatusEnum.STOP.equals(po.getStatus())
                                                 || InspectTaskStatusEnum.REPAIRED.equals(po.getStatus())));
            message.setOperationType(TodoTaskFlowOperationEnum.CHANGE);
            message.setNextBusinessStatus(po.getStatus().name());
            message.setNextBusinessStatusName(po.getStatus().getDescription());
            message.setNextUserIds(Arrays.stream(po.getReceiveIds().split(",")).map(Long::valueOf).collect(Collectors.toList()));
            //发消息
            RocketMQContextHolder.syncSendOrderly(TodoTaskFlowCirculationMessage.TOPIC, message, String.valueOf(po.getId()));

            WorkTaskFlowCirculationMessage workTaskMessage = new WorkTaskFlowCirculationMessage();
            workTaskMessage.setBusinessKey(String.valueOf(po.getId()));
            workTaskMessage.setBusinessStatus(oldTaskStatus.name());
            workTaskMessage.setUserIds(userIds);
            workTaskMessage.setHandlerTime(LocalDateTime.now());
            workTaskMessage.setTaskTime(LocalDateTime.now());
            workTaskMessage.setGeneratedNextStatusTask(!(InspectTaskStatusEnum.OVER.equals(po.getStatus())
                                                         || InspectTaskStatusEnum.STOP.equals(po.getStatus())
                                                         || InspectTaskStatusEnum.EXPIRE.equals(po.getStatus())));
            workTaskMessage.setOperationType(WorkTaskFlowOperationEnum.CHANGE);
            workTaskMessage.setNextBusinessStatus(po.getStatus().name());
            workTaskMessage.setNextBusinessStatusName(po.getStatus().getDescription());
            workTaskMessage.setNextUserIds(Arrays.stream(po.getReceiveIds().split(",")).map(Long::valueOf).collect(Collectors.toList()));
            //发消息
            RocketMQContextHolder.syncSendOrderly(WorkTaskFlowCirculationMessage.TOPIC, workTaskMessage, String.valueOf(po.getId()));

        }

    }

}
