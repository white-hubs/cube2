package com.guhecloud.cube.srv.gen.maintain.server.upkeep.commandgateway.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseDetailsDTO;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.base.message.sdk.MessageSendClient;
import com.guhecloud.cube.base.message.sdk.api.request.MessageSpecifyTmpSendRequest;
import com.guhecloud.cube.base.message.sdk.api.response.MessageSendResponse;
import com.guhecloud.cube.biz.ouaa.client.ApplicationClient;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.ApplicationDTO;
import com.guhecloud.cube.biz.todo.client.enumeration.TodoTaskFlowOperationEnum;
import com.guhecloud.cube.biz.todo.client.message.TodoTaskInitMessage;
import com.guhecloud.cube.biz.work.center.client.enumeration.WorkTaskFlowOperationEnum;
import com.guhecloud.cube.biz.work.center.client.message.WorkTaskInitMessage;
import com.guhecloud.cube.srv.gen.maintain.server.common.enumeration.MaintainApplicationEnum;
import com.guhecloud.cube.srv.gen.maintain.server.common.util.MessageSendUtil;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.commandgateway.MaintenanceProgetRecordCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.commandgateway.MaintenanceTasksCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.commandgateway.command.MaintenancePlanRuleCommand;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.dao.MaintenanceObjectMapper;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.dao.MaintenancePlanMapper;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.dao.MaintenanceRuleConditionMapper;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.dao.MaintenanceTasksMapper;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.enumeration.GenerativeRulesEnum;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.enumeration.MaintenaceConditionTypeEnum;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.enumeration.MaintenaceDetailsEnum;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.enumeration.MaintenacePlanStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.enumeration.MaintenaceTaskStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.interfaces.vo.MaintenaceVo;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.interfaces.vo.MaintenanceProjectVo;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.po.MaintenanceObject;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.po.MaintenancePlan;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.po.MaintenanceProgetRecord;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.po.MaintenanceTasks;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.rocketmq.produce.MaintainEventProduce;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.guhecloud.platform.base.util.DateUtils.PURE_DATE_FORMAT;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MaintenanceTasksCommandServiceImpl extends ServiceImpl<MaintenanceTasksMapper, MaintenanceTasks> implements MaintenanceTasksCommandService {

    private final MaintenancePlanMapper maintenancePlanMapper;

    private final MaintenanceObjectMapper maintenanceObjectMapper;

    private final MaintenanceTasksMapper maintenanceTasksMapper;

    private final MaintenanceProgetRecordCommandService maintenanceProgetRecordCommandService;

    private final UserClient userClient;

    private final ApplicationClient applicationClient;
    private final MaintenanceRuleConditionMapper ruleConditionMapper;
    private final DeviceCenterStdClient deviceCenterStdClient;

    /**
     * 获取当前时间发布任务计划
     * <p>
     * 过滤执行计划
     * <p>
     * 根据执行计划构建运维任务并发布
     * 构建
     * 发布
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveTask() {
        //获取当前时间发布任务计划
        String currentTime = getCurrentTime();
        List<MaintenancePlan> maintenancePlans = maintenancePlanMapper.selectList(Wrappers.<MaintenancePlan>lambdaQuery()
                                                                                          .eq(MaintenancePlan::getNextTaskTime, currentTime)
                                                                                          .eq(MaintenancePlan::getStatus, MaintenacePlanStatusEnum.ENABLED.name())
                                                                                 );
        // 获取规则
        if (!maintenancePlans.isEmpty()) {
            var date = LocalDateTime.now();
            String formatTime = DateUtils.format(date);
            maintenancePlans.forEach(s -> {
                SessionContextHolder.requestTenant(s.getTenant());
                if (GenerativeRulesEnum.RULE.name().equals(s.getWay())) {
                    List<MaintenancePlanRuleCommand> planRuleCommandList = ruleConditionMapper.selectAllByPlanId(s.getId(), MaintenaceConditionTypeEnum.DEVICE_DETAILS.name());
                    if (ObjectUtils.isNotEmpty(planRuleCommandList)) {
                        Set<Long> deviceIds = maintenanceObjectMapper.selectAllByPlanId(s.getId());
                        List<DeviceBaseDetailsDTO> deviceBaseDetailsList = deviceCenterStdClient.getDetailsListByDeviceId(deviceIds);
                        deviceBaseDetailsList.forEach(device -> {
                            planRuleCommandList.forEach(ex -> {
                                ex.getEmergencyRuleConditionExps().forEach(exps -> {
                                    var oprand = Integer.parseInt(exps.getOprand());
                                    if (MaintenaceDetailsEnum.EQ.name().equals(exps.getOp())) {
                                        if (oprand == DateUtils.between(device.getCreateTime(), LocalDateTime.now(), ChronoUnit.DAYS)) {
                                            saveTask(formatTime, s);
                                        }
                                    } else if (MaintenaceDetailsEnum.GT.name().equals(exps.getOp())) {
                                        if (oprand >= DateUtils.between(device.getCreateTime(), LocalDateTime.now(), ChronoUnit.DAYS)) {
                                            saveTask(formatTime, s);
                                        }
                                    }
                                });
                                //                                saveTask(formatTime, s);
                            });
                        });
                    }
                } else {
                    //判断已发布任务是否全部执行完
                    Integer taskFlag = maintenanceTasksMapper.getTaskFlag(s.getId());
                    if (taskFlag == null || taskFlag == 1) {
                        saveTask(formatTime, s);
                    }
                    String nextime = "";
                    String timePoint;
                    if (s.getMaintenanceTimePoint() < 10) {
                        timePoint = "0" + s.getMaintenanceTimePoint();
                    } else {
                        timePoint = String.valueOf(s.getMaintenanceTimePoint());
                    }
                    switch (s.getTimeUnit()) {
                        case "p-day": {
                            var time = date.plusDays(s.getMaintenanceCycle());
                            String format1 = DateUtils.format(time, PURE_DATE_FORMAT);
                            nextime = format1 + timePoint;
                            break;
                        }
                        case "p-week": {
                            var time = date.plusWeeks(s.getMaintenanceCycle());
                            String format1 = DateUtils.format(time, PURE_DATE_FORMAT);
                            nextime = format1 + timePoint;
                            break;
                        }
                        case "p-month": {
                            var time = date.plusMonths(s.getMaintenanceCycle());
                            String format1 = DateUtils.format(time, PURE_DATE_FORMAT);
                            nextime = format1 + timePoint;
                            break;
                        }
                    }
                    s.setNextTaskTime(nextime);
                    maintenancePlanMapper.updateById(s);
                }
            });
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePlan() {
        var date = LocalDateTime.now();
        String format1 = DateUtils.formatDate(date);
        List<MaintenancePlan> maintenancePlans = maintenancePlanMapper.selectList(Wrappers.<MaintenancePlan>lambdaQuery()
                                                                                          .eq(MaintenancePlan::getPlanEndTime, format1));
        if (!maintenancePlans.isEmpty()) {
            List<Long> collect = maintenancePlans.stream().map(MaintenancePlan::getId).collect(Collectors.toList());
            maintenancePlanMapper.updatePlan(collect);
        }
    }

    private String getCurrentTime() {
        var date = LocalDateTime.now();
        String format1 = DateUtils.format(date, "HH");
        String format3 = DateUtils.format(date, PURE_DATE_FORMAT);
        return format3 + format1;
    }

    //构建消息信息
    private void sendMsg(MaintenanceTasks maintenanceTasks, MaintenancePlan maintenancePlan) {
        HashMap<String, String> msgParams = new HashMap<>();
        msgParams.put("maintenaceEvent", "维保作业");
        msgParams.put("maintenaceName", maintenanceTasks.getObjectName());
        Set<Long> notifyUserId = getNotifyUserIds(maintenancePlan);
        MessageSpecifyTmpSendRequest message = MessageSpecifyTmpSendRequest.builder()
                .tenant(SessionContextHolder.getRequester().getTenant())
                .applicationCode("MAINTAIN_TASK_MSG")
                .msgSceneCode("MAINTAIN_MAINTANCE_RECEIPT_CONFIRM")
                .businessId(maintenanceTasks.getId().toString())
                .tmpParams(msgParams)
                .messageNotifyTargets(MessageSendUtil.getNotifyUserByIds(notifyUserId))
                .build();
        MessageSendClient sendClient = ApplicationContextHolder.getBean(MessageSendClient.class);
        MessageSendResponse messageSendResponse;
        try {
            messageSendResponse = sendClient.sceneSend(message);
            log.info("维保作业消息发送结果,{}", JSON.toJSONString(messageSendResponse));
        } catch (Exception e) {
            log.error("维保作业消息发送失败:{}", e.getMessage());
        }
    }

    private Set<Long> getNotifyUserIds(MaintenancePlan maintenancePlan) {
        Set<Long> userIds = new HashSet<>();
        if (maintenancePlan.getResponsibilityType().equals("per")) {
            String responsibilityPersonId = maintenancePlan.getResponsibilityPersonId();
            String[] split = responsibilityPersonId.split("-");
            for (String s : split) {
                userIds.add(Long.valueOf(s));
            }
        } else {
            ArrayList<Long> orgIds = new ArrayList<>();
            ArrayList<Long> postIds = new ArrayList<>();
            String receiveDeptId = maintenancePlan.getReceiveDeptId();
            String[] split = receiveDeptId.split("-");
            for (String s : split) {
                orgIds.add(Long.valueOf(s));
            }
            String receivePostId = maintenancePlan.getReceivePostId();
            String[] split1 = receivePostId.split("-");
            for (String s : split1) {
                postIds.add(Long.valueOf(s));
            }
            List<Long> longs = userClient.listEnableUserByOrgAndPost(orgIds, postIds);
            userIds.addAll(longs);
        }
        return userIds;
    }

    /**
     * @Description 代办中心推送
     * @Author lrj
     */
    private void agency(MaintenaceVo maintenaceVo, MaintenancePlan maintenancePlan) {
        //获取应用
        ApplicationDTO applicationDTO = applicationClient.getApplicationByCode(MaintainApplicationEnum.MAINTENACE.name());

        //获取任务处理人id
        ArrayList<Long> userIds = new ArrayList<>();
        if (maintenancePlan.getResponsibilityType().equals("per")) {
            String responsibilityPersonId = maintenancePlan.getResponsibilityPersonId();
            String[] split = responsibilityPersonId.split("-");
            for (String s : split) {
                userIds.add(Long.valueOf(s));
            }
        } else {
            ArrayList<Long> orgIds = new ArrayList<>();
            ArrayList<Long> postIds = new ArrayList<>();
            String receiveDeptId = maintenancePlan.getReceiveDeptId();
            String[] split = receiveDeptId.split("-");
            for (String s : split) {
                orgIds.add(Long.valueOf(s));
            }
            String receivePostId = maintenancePlan.getReceivePostId();
            String[] split1 = receivePostId.split("-");
            for (String s : split1) {
                postIds.add(Long.valueOf(s));
            }

            List<Long> longs;
            if (orgIds.size() == 0 && postIds.size() == 0) {
                longs = new ArrayList<>();
            } else {
                longs = userClient.listEnableUserByOrgAndPost(orgIds, postIds);
            }
            userIds.addAll(longs);
        }

        List<Long> collect = userIds.stream().distinct().collect(Collectors.toList());

        //创建消息体
        TodoTaskInitMessage todoTaskMessage = TodoTaskInitMessage.builder()
                .applicationCode(applicationDTO.getCode())
                .applicationName(applicationDTO.getName())
                .businessKey(String.valueOf(maintenaceVo.getId()))
                .businessType(MaintainApplicationEnum.MAINTENACE.name())
                .taskName(maintenaceVo.getName())
                .taskTime(LocalDateTime.now())
                .userIds(collect)
                .businessStatus(maintenaceVo.getStatus())
                .businessTypeName("维保作业")
                .businessStatusName(maintenaceVo.getStatusName())
                .operationType(TodoTaskFlowOperationEnum.INSERT)
                .build();

        //发消息
        RocketMQContextHolder.syncSendOrderly(TodoTaskInitMessage.TOPIC, todoTaskMessage, String.valueOf(maintenaceVo.getId()));
        sendWorkInitMsg(applicationDTO, maintenaceVo, collect);
    }

    private void sendWorkInitMsg(ApplicationDTO applicationDTO, MaintenaceVo maintenaceVo, List<Long> userIds) {
        //创建消息体
        WorkTaskInitMessage todoTaskMessage = WorkTaskInitMessage.builder()
                .applicationCode(applicationDTO.getCode())
                .applicationName(applicationDTO.getName())
                .businessKey(String.valueOf(maintenaceVo.getId()))
                .businessType(MaintainApplicationEnum.MAINTENACE.name())
                .taskName(maintenaceVo.getName())
                .taskTime(LocalDateTime.now())
                .userIds(userIds)
                .businessStatus(maintenaceVo.getStatus())
                .businessTypeName("维保作业")
                .businessStatusName(maintenaceVo.getStatusName())
                .operationType(WorkTaskFlowOperationEnum.INSERT)
                .build();
        //发消息
        RocketMQContextHolder.syncSendOrderly(WorkTaskInitMessage.TOPIC, todoTaskMessage, String.valueOf(maintenaceVo.getId()));
    }

    /**
     * 生成任务,发送消息、代办
     */
    public void saveTask(String formatTime, MaintenancePlan plan) {
        SessionContextHolder.requestTenant(plan.getTenant());
        //发布任务
        List<MaintenanceObject> maintenanceObjects = maintenanceObjectMapper.selectList(Wrappers.<MaintenanceObject>lambdaQuery()
                                                                                                .eq(MaintenanceObject::getPlanId, plan.getId()));
        maintenanceObjects.forEach(obj -> {
            long id = IdWorker.getId();
            MaintenanceTasks maintenanceTasks = new MaintenanceTasks();
            maintenanceTasks.setId(id);
            maintenanceTasks.setCode("SQ" + id);
            maintenanceTasks.setMaintenanceTime(formatTime);
            maintenanceTasks.setPlanId(obj.getPlanId());
            maintenanceTasks.setMaintenanceTaskStatus(MaintenaceTaskStatusEnum.BE_MAINTAINED.name());
            maintenanceTasks.setObjectId(obj.getKeyId());
            maintenanceTasks.setObjectName(obj.getObjectName());
            maintenanceTasks.setCategoryCode(obj.getCategoryCode());
            maintenanceTasks.setCategoryName(obj.getCategoryName());
            maintenanceTasksMapper.insert(maintenanceTasks);
            List<MaintenanceProjectVo> objectProjects = maintenanceTasksMapper.getObjectProjects(obj.getId());
            ArrayList<MaintenanceProgetRecord> maintenanceProgetRecords = new ArrayList<>();
            objectProjects.forEach(obp -> {
                MaintenanceProgetRecord maintenanceProgetRecord = new MaintenanceProgetRecord();
                maintenanceProgetRecord.setTaskId(id);
                maintenanceProgetRecord.setMaintrenaceProject(obp.getMaintenanceContent());
                maintenanceProgetRecords.add(maintenanceProgetRecord);
            });
            maintenanceProgetRecordCommandService.saveBatch(maintenanceProgetRecords);

            //推送设备生命周期信息
            MaintenaceVo maintenaceVo = new MaintenaceVo();
            maintenaceVo.setTenant(plan.getTenant());
            maintenaceVo.setId(id);
            maintenaceVo.setName(plan.getMaintenancePlanName());
            maintenaceVo.setStatus(MaintenaceTaskStatusEnum.BE_MAINTAINED.name());
            maintenaceVo.setStatusName(MaintenaceTaskStatusEnum.BE_MAINTAINED.getDescription());
            maintenaceVo.setObjId(obj.getKeyId());
            MaintainEventProduce.sendFaultEvent(maintenaceVo);
            //发消息
            sendMsg(maintenanceTasks, plan);
            //代办中心推送
            agency(maintenaceVo, plan);
        });
    }
}
