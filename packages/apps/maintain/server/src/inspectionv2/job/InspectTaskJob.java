package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.job;

import com.alibaba.fastjson2.JSON;
import com.guhecloud.cube.base.bracing.api.AssemblyNoticeClient;
import com.guhecloud.cube.base.bracing.dto.data.MessageSendConfig;
import com.guhecloud.cube.base.bracing.dto.data.MessageSendParam;
import com.guhecloud.cube.base.bracing.dto.data.MessageSendSceneQueryPram;
import com.guhecloud.cube.base.bracing.dto.request.AssemblyNoticeObjDTO;
import com.guhecloud.cube.base.message.sdk.api.response.MessageSendResponse;
import com.guhecloud.cube.biz.todo.client.TodoTaskClient;
import com.guhecloud.cube.biz.work.center.client.WorkTaskClient;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.HandlerService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.PlanService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.TaskService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.InspectPlanCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.InspectTaskCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.impl.InspectQuery;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectNoticeModuleTypeEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectPlanExecStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectTaskStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectionMappingEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.filter.BusinessTypeInterceptor;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectPlanPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectTaskPO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InspectTaskJob {
    @Autowired
    private InspectPlanCommandService planCommandService;

    @Autowired
    private InspectTaskCommandService taskCommandService;

    @Resource
    private AssemblyNoticeClient assemblyNoticeClient;
    @Resource
    private HandlerService handlerService;
    @Resource
    private PlanService planService;
    @Resource
    private TaskService taskService;
    @Resource
    private InspectQuery inspectQuery;
    @Resource
    private TodoTaskClient todoTaskClient;
    @Resource
    private WorkTaskClient workTaskClient;

    public void inspectionTaskJobHandler(String planId) {
        var id = Long.valueOf(planId);
        try {
            //查询巡检计划详情
            BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.set("IGNORE");
            var planPO = planService.getById(id);
            BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.remove();
            if (planPO == null) {
                log.error("巡检计划执行失败-->计划ID：{},找不到该计划或该计划已经删除，请确认后，手动删除该定时计划！", id);
                return;
            } else if (planPO.getStatus().name().equals(InspectPlanExecStatusEnum.STOP.name())) {
                log.error("巡检计划执行失败-->计划ID：{},计划已经被暂停！", id);
                planCommandService.stopPlan(id);
                return;
            } else if (planPO.getStatus().name().equals(InspectPlanExecStatusEnum.OVER.name())) {
                log.error("巡检计划执行失败-->计划ID：{},计划已经结束！", id);
                //结束该计划
                planCommandService.overPlan(id);
                return;
            }
            //设置业务类型
            if (StringUtils.isNotBlank(planPO.getBusinessType()) &&
                StringUtils.isBlank(BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.get())) {
                BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.set(planPO.getBusinessType());
            }
            //检查是否达到开始时间
            var beginTime = planPO.getBeginTime();
            var endTime = planPO.getEndTime();
            var currentTime = LocalDateTime.now();
            if (DateUtils.toTimestamp(currentTime) - DateUtils.toTimestamp(beginTime) >= 0 &&
                (endTime == null || DateUtils.toTimestamp(currentTime) - DateUtils.toTimestamp(endTime) <= 0)) {
                // 重设计划执行次数
                planPO.setExecTimes(planPO.getExecTimes() == null ? 1 : planPO.getExecTimes() + 1);
                SessionContextHolder.requestTenant(planPO.getTenant());
                planService.updateById(planPO);
                // 创建任务
                taskService.createTaskByPlan(planPO);

                log.info("巡检计划执行成功-->计划ID：{}", id);
            } else if (DateUtils.toTimestamp(currentTime) - DateUtils.toTimestamp(endTime) > 0) {
                //结束该计划
                planCommandService.overPlan(id);
            }
        } catch (Exception e) {
            log.error("巡检计划执行失败-->计划ID：{},失败原因:", id, e);
            throw new ServiceException(e.getMessage());
        } finally {
            BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.remove();
        }
    }

    //每小时检测一次 0 0 0/1 * * ?
    @JobAnnotation(name = "InspectionTaskExpireJobHandler", cron = "30 */1 * * * ?", desc = "日常巡检-检查巡检任务是否失效")
    public void inspectionTaskExpireJobHandler(String param) {
        try {
            // 检测计划失效
            BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.set("IGNORE");
            var newDate = LocalDateTime.now();
            var planList = planCommandService.lambdaQuery()
                    .eq(InspectPlanPO::getStatus, InspectPlanExecStatusEnum.EXECUTING)
                    .lt(InspectPlanPO::getValidEndTime, newDate)
                    .list();
            Optional.ofNullable(planList).orElse(Collections.emptyList())
                    .forEach(planPO -> {
                        BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.set(planPO.getBusinessType());
                        SessionContextHolder.requestTenant(planPO.getTenant());
                        planPO.setStatus(InspectPlanExecStatusEnum.OVER);
                        // 设置状态为结束
                        planCommandService.updateById(planPO);
                        // 结束xxjob任务
                        if (planPO.getXxlTaskId() != null && planPO.getXxlTaskId() != 0) {
                            planService.removePlanJob(planPO.getXxlTaskId().toString());
                        }
                    });
            // 检测任务失效
            BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.set("SMALL_ENGINEERING");
            List<InspectTaskPO> taskPOS = taskCommandService.lambdaQuery()
                    .le(InspectTaskPO::getValidEndTime, LocalDateTime.now())
                    .and(i -> i.eq(InspectTaskPO::getStatus, InspectTaskStatusEnum.SPLIT)
                            .or().eq(InspectTaskPO::getStatus, InspectTaskStatusEnum.EXECUTING)
                            .or().eq(InspectTaskPO::getStatus, InspectTaskStatusEnum.ACCEPT))
                    .list();
            Optional.ofNullable(taskPOS).orElse(Collections.emptyList()).forEach(taskPO -> {
                BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.set(taskPO.getBusinessType());
                SessionContextHolder.requestTenant(taskPO.getTenant());
                taskCommandService.expire(taskPO);
            });
            BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.remove();
        } catch (Exception e) {
            log.error("巡检计划失效检测失败,失败原因:", e);
        } finally {
            SessionContextHolder.requestTenant(null);
            BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.remove();
        }
        try {
            this.expireInspectTaskTodo();
        } catch (Exception e) {
            log.error("失效巡检任务待办失败,失败原因:", e);
        }
    }

    private void expireInspectTaskTodo() {
        // 获取所有任务
        var taskPOS = taskCommandService.lambdaQuery()
                .le(InspectTaskPO::getValidEndTime, LocalDateTime.now())
                .and(i -> i.eq(InspectTaskPO::getStatus, InspectTaskStatusEnum.SPLIT)
                        .or().eq(InspectTaskPO::getStatus, InspectTaskStatusEnum.EXECUTING)
                        .or().eq(InspectTaskPO::getStatus, InspectTaskStatusEnum.ACCEPT))
                .eq(InspectTaskPO::getExpireTodo, false)
                .list();
        if (CollectionUtils.isEmpty(taskPOS)) {
            return;
        }
        SecurityContextHolder.setPrincipal(Principal.builder().id(1L).name("system").account("system").type(Principal.Type.CLIENT)
                                                   .build());
        taskPOS.stream().collect(Collectors.groupingBy(InspectTaskPO::getTenant)).forEach((tenant, pos) -> {
            SessionContextHolder.requestTenant(tenant);
            var taskIdSet = new HashSet<Long>();
            var taskIds = new HashSet<String>();
            pos.forEach(taskPo -> {
                taskIdSet.add(taskPo.getId());
                taskIds.add(String.valueOf(taskPo.getId()));
            });
            todoTaskClient.removeBatchByBusinessIds(taskIds);
            workTaskClient.removeBatchByBusinessIds(taskIds);
            taskCommandService.lambdaUpdate().set(InspectTaskPO::getExpireTodo, true).in(InspectTaskPO::getId, taskIdSet).update();
        });
    }

    /**
     * 任务通知检测 每分钟执行一次
     *
     * @param param 暂未使用的入参
     *
     * @return
     */
    // * * * * * ? * 每分钟执行一次
    @JobAnnotation(name = "inspectionTaskNoticeJobHandler", cron = "0 */1 * * * ?", desc = "日常巡检-巡检任务失效通知")
    public void inspectionTaskNoticeJobHandler(String param) {
        // 通知检测
        try {
            //获取租户
            var tenantList = taskService.getTaskTenantList();
            var localDate = LocalDate.now();
            var localTime = LocalTime.now();
            var startDate = LocalDateTime.of(localDate, localTime.withSecond(0).withNano(0));
            var endDate = LocalDateTime.of(localDate, localTime.withSecond(59));
            var sceneList = Arrays.asList(InspectNoticeModuleTypeEnum.TASK_LAPSE.name(), InspectNoticeModuleTypeEnum.TASK_LAPSE_UNFINISHED.name());
            var businessTypes = Arrays.stream(InspectionMappingEnum.values()).map(InspectionMappingEnum::name)
                    .collect(Collectors.joining(","));
            Optional.ofNullable(tenantList).orElse(Collections.emptyList()).forEach(tenantCode -> {
                SessionContextHolder.requestTenant(tenantCode.intValue());
                // 获取任务失效通知配置
                var messageSendConfigs = assemblyNoticeClient.getSendMsgConfig(MessageSendSceneQueryPram.builder()
                                                                                       .businessType(businessTypes)
                                                                                       .sceneCodes(sceneList)
                                                                                       .build());
                if (CollectionUtils.isNotEmpty(messageSendConfigs)) {
                    var inspectConfigMap = messageSendConfigs.stream()
                            .collect(Collectors.groupingBy(MessageSendConfig::getSceneCode));
                    for (var entry : inspectConfigMap.entrySet()) {
                        if (InspectNoticeModuleTypeEnum.TASK_LAPSE.name().equals(entry.getKey())) {
                            taskLapseSendMessage(entry.getValue(), startDate, endDate);
                        } else if (InspectNoticeModuleTypeEnum.TASK_LAPSE_UNFINISHED.name().equals(entry.getKey())) {
                            taskLapseUnfinishedSendMessage(entry.getValue(), startDate, endDate);
                        }
                    }
                }
            });

        } catch (Exception e) {
            log.error("巡检计划失效检测失败,失败原因:", e);
        } finally {
            SessionContextHolder.requestTenant(null);
            BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.remove();
        }
    }

    /**
     * 任务未完成通知
     *
     * @param taskUnfinishedConfig
     * @param startDate
     * @param endDate
     */
    private void taskLapseUnfinishedSendMessage(List<MessageSendConfig> taskUnfinishedConfig, LocalDateTime startDate,
                                                LocalDateTime endDate
                                               ) {
        log.info("{}未完成通知配置：{}", SessionContextHolder.getRequester().getTenant(), taskUnfinishedConfig);
        taskUnfinishedConfig.forEach(config -> {
            InspectionMappingEnum businessTypeEnum = InspectionMappingEnum.getInspectionType(config.getBusinessType());
            BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.set("IGNORE");
            List<InspectTaskPO> taskPOList = taskService.lambdaQuery()
                    .eq(InspectTaskPO::getIsSplit, false)
                    .between(InspectTaskPO::getValidEndTime, startDate, endDate)
                    .and(item -> item.eq(InspectTaskPO::getStatus, InspectTaskStatusEnum.EXECUTING)
                            .or().eq(InspectTaskPO::getStatus, InspectTaskStatusEnum.ACCEPT)
                            .or().eq(InspectTaskPO::getStatus, InspectTaskStatusEnum.EXPIRE)
                        ).list();
            BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.remove();
            if (ObjectUtils.isNotEmpty(taskPOList)) {
                List<AssemblyNoticeObjDTO> noticeObjList = config.getNoticeObjList();
                Map<String, Set<Long>> noticeMap = new HashMap<>();
                if (ObjectUtils.isNotEmpty(noticeObjList)) {
                    noticeObjList.forEach(notice -> {
                        Set<Long> set = new HashSet<>();
                        Arrays.stream(notice.getObjId().split(",")).mapToLong(Long::parseLong).forEach(set::add);
                        noticeMap.put(notice.getObjType(), set);
                    });
                }
                for (InspectTaskPO taskPO : taskPOList) {
                    BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.set(taskPO.getBusinessType());
                    String receiveNames = inspectQuery.extendUserNames(taskPO.getTenant(), taskPO.getReceiveIds());
                    Map<String, String> paramMap = new HashMap<>();
                    paramMap.put("taskName", taskPO.getName());
                    paramMap.put("content", receiveNames);
                    Set<Long> longs = noticeMap.get(String.valueOf(taskPO.getTypeId()));
                    if (ObjectUtils.isEmpty(longs)) {
                        longs = handlerService.stringIdsToList(taskPO.getReceiveIds());
                    }
                    MessageSendResponse response = assemblyNoticeClient.sendMsg(MessageSendParam.builder()
                                                                                        .businessId(String.valueOf(taskPO.getId()))
                                                                                        .notifyId(config.getNotifyId())
                                                                                        .msgSceneCode(businessTypeEnum.getMsgSceneCode(config.getSceneCode()))
                                                                                        .notifyUsers(longs)
                                                                                        .tempParams(paramMap)
                                                                                        .build());
                    log.info("巡检任务未完成消息发送结果:{}", JSON.toJSONString(response));
                    BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.remove();
                }
            }
            BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.remove();
        });
    }

    /**
     * 任务失效通知
     *
     * @param taskLapseConfig
     * @param startDate
     * @param endDate
     */
    private void taskLapseSendMessage(List<MessageSendConfig> taskLapseConfig, LocalDateTime startDate, LocalDateTime endDate) {
        log.info("{}失效通知配置：{}", SessionContextHolder.getRequester().getTenant(), taskLapseConfig);
        taskLapseConfig.forEach(config -> {
            InspectionMappingEnum businessTypeEnum = InspectionMappingEnum.getInspectionType(config.getBusinessType());
            Integer time = config.getValue();
            var offsetStart = DateUtils.offset(startDate, ChronoUnit.MINUTES, time);
            var offsetEnd = DateUtils.offset(endDate, ChronoUnit.MINUTES, time);
            BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.set("IGNORE");
            List<InspectTaskPO> taskPOList = taskService.lambdaQuery()
                    .eq(InspectTaskPO::getIsSplit, false)
                    .between(InspectTaskPO::getValidEndTime, offsetStart, offsetEnd)
                    .eq(InspectTaskPO::getStatus, InspectTaskStatusEnum.ACCEPT)
                    .list();
            BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.remove();
            if (ObjectUtils.isNotEmpty(taskPOList)) {
                for (InspectTaskPO taskPO : taskPOList) {
                    BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.set(taskPO.getBusinessType());
                    Set<Long> ids = handlerService.stringIdsToList(taskPO.getReceiveIds());
                    Map<String, String> paramMap = new HashMap<>();
                    paramMap.put("taskName", taskPO.getName());
                    paramMap.put("content", String.valueOf(time));
                    MessageSendResponse response = assemblyNoticeClient.sendMsg(MessageSendParam.builder()
                                                                                        .businessId(String.valueOf(taskPO.getId()))
                                                                                        .notifyId(config.getNotifyId())
                                                                                        .msgSceneCode(businessTypeEnum.getMsgSceneCode(config.getSceneCode()))
                                                                                        .notifyUsers(ids)
                                                                                        .tempParams(paramMap)
                                                                                        .build());
                    log.info("巡检任务失效消息发送结果:{}", JSON.toJSONString(response));
                    BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.remove();
                }
            }
            BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.remove();
        });
    }
}
