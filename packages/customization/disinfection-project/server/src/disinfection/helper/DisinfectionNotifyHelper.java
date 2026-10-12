package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.helper;

import com.guhecloud.cube.base.bracing.api.AssemblyNoticeClient;
import com.guhecloud.cube.base.bracing.dto.data.MessageSendConfig;
import com.guhecloud.cube.base.bracing.dto.data.MessageSendParam;
import com.guhecloud.cube.base.bracing.dto.data.MessageSendSceneQueryPram;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity.DisinfectionNotifyRecord;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity.DisinfectionTask;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.DisinfectionNotificationTypeEnum;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.DisinfectionNotifySceneCodeEnum;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.job.dto.NotifyTaskDTO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionGroupUserService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionNotifyRecordService;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.MapUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class DisinfectionNotifyHelper {
    private final static String DISINFECTION_APPLICATION_CODE = "PROJECT_MANAGE";
    private final DisinfectionGroupUserService groupUserService;
    private final AssemblyNoticeClient assemblyNoticeClient;
    private final DisinfectionUserHelper disinfectionUserHelper;
    private final DisinfectionNotifyRecordService disinfectionNotifyRecordService;

    public void sendDisinfectionTaskCreateMessage(DisinfectionTask disinfectionTask) {
        try {
            MessageSendSceneQueryPram queryPram = MessageSendSceneQueryPram.builder()
                    .businessType(DISINFECTION_APPLICATION_CODE)
                    .sceneCodes(Collections.singletonList(DisinfectionNotifySceneCodeEnum.TASK_CREATE.getCode()))
                    .build();
            List<MessageSendConfig> sendMsgConfigs = assemblyNoticeClient.getSendMsgConfig(queryPram);
            if (ListUtils.isEmpty(sendMsgConfigs)) {
                log.error("任务创建，没有找到对应的通知模板");
                return;
            }
            //创建待保存的任务通知创建记录
            List<DisinfectionNotifyRecord> waitSaveNotifyRecords = new ArrayList<>();
            //获取该场景下的通知记录
            List<DisinfectionNotifyRecord> records = disinfectionNotifyRecordService.listBySceneCode(DisinfectionNotifySceneCodeEnum.TASK_LAPSE_UNFINISHED);
            Map<Long, List<DisinfectionNotifyRecord>> disinfectionNotifyRecordMap = MapUtils.listGroupToMap(records, DisinfectionNotifyRecord::getBusinessId);
            Set<Long> notifyUserIds = new HashSet<>();
            DisinfectionNotificationTypeEnum notificationType = disinfectionTask.getNotificationType();

            if (DisinfectionNotificationTypeEnum.GROUP.equals(notificationType)) {
                //获取下的成员
                Long notificationGroupId = disinfectionTask.getNotificationGroupId();
                List<Long> userIds = groupUserService.getGroupUserIds(notificationGroupId);
                notifyUserIds.addAll(userIds);
                Assert.isParameterError(ListUtils.isEmpty(notifyUserIds), "当前组内没有成员，请选择有用户的小组");
            }
            if (DisinfectionNotificationTypeEnum.GROUP_ROLE.equals(notificationType)) {
                Long notificationGroupId = disinfectionTask.getNotificationGroupId();
                List<Long> userIds = groupUserService.getGroupUserIds(notificationGroupId);
                //获取用户角色信息
                List<UserDTO> users = disinfectionUserHelper.getUsers(userIds);
                //获取通知类型的角色
                Long notificationRoleId = disinfectionTask.getNotificationRoleId();
                //保留特定的角色的用户
                Set<Long> userIdSet = users
                        .stream()
                        .filter(userDTO -> {
                            List<UserDTO.Role> roles = userDTO.getRoles();
                            Set<Long> roleIdSet = roles.stream().map(UserDTO.Role::getId).collect(Collectors.toSet());
                            return roleIdSet.contains(notificationRoleId);
                        })
                        .map(UserDTO::getId)
                        .collect(Collectors.toSet());
                notifyUserIds.addAll(userIdSet);
                Assert.isParameterError(ListUtils.isEmpty(userIdSet), "当前组内没有该角色的成员，请选择有该角色的小组");
            }
            if (DisinfectionNotificationTypeEnum.DEPT.equals(notificationType)) {
                Long notificationDeptId = disinfectionTask.getNotificationDeptId();
                List<UserDTO> users = disinfectionUserHelper.getUserByDeptId(notificationDeptId);
                notifyUserIds.addAll(users.stream().map(UserDTO::getId).collect(Collectors.toSet()));
                Assert.isParameterError(ListUtils.isEmpty(notifyUserIds), "当前部门没有成员，请选择有成员的部门");
            }
            if (DisinfectionNotificationTypeEnum.ROLE.equals(notificationType)) {
                Long notificationRoleId = disinfectionTask.getNotificationRoleId();
                List<UserDTO> users = disinfectionUserHelper.getUserByRoleId(notificationRoleId);
                notifyUserIds.addAll(users.stream().map(UserDTO::getId).collect(Collectors.toSet()));
                Assert.isParameterError(ListUtils.isEmpty(notifyUserIds), "当前角色没有成员，请选择有成员的角色");
            }
            if (DisinfectionNotificationTypeEnum.POST.equals(notificationType)) {
                Long notificationPostId = disinfectionTask.getNotificationPostId();
                List<UserDTO> users = disinfectionUserHelper.getUserByPostId(notificationPostId);
                notifyUserIds.addAll(users.stream().map(UserDTO::getId).collect(Collectors.toSet()));
                Assert.isParameterError(ListUtils.isEmpty(notifyUserIds), "当前岗位没有成员，请选择有成员的岗位");
            }
            Map<String, String> temParamMap = new HashMap<>();
            temParamMap.put("taskName", disinfectionTask.getTaskName());
            sendMsgConfigs.forEach(sendMsgConfig -> {
                if (isNotified(DisinfectionNotifySceneCodeEnum.TASK_CREATE, sendMsgConfig, disinfectionTask.getId(), disinfectionNotifyRecordMap)) {
                    return;
                }
                MessageSendParam sendParam = MessageSendParam.builder()
                        .businessId(disinfectionTask.getId().toString())
                        .applicationCode(DISINFECTION_APPLICATION_CODE)
                        .notifyId(sendMsgConfig.getNotifyId())
                        .msgSceneCode(DisinfectionNotifySceneCodeEnum.TASK_CREATE.getCode())
                        .notifyUsers(notifyUserIds)
                        .tempParams(temParamMap)
                        .build();
                assemblyNoticeClient.sendMsg(sendParam);
                DisinfectionNotifyRecord notifyRecord = new DisinfectionNotifyRecord();
                notifyRecord.setNotifyUsers(notifyUserIds.stream().map(String::valueOf).collect(Collectors.joining(",")));
                notifyRecord.setTemplateId(sendMsgConfig.getNotifyId());
                notifyRecord.setSceneCode(DisinfectionNotifySceneCodeEnum.TASK_CREATE);
                notifyRecord.setBusinessId(disinfectionTask.getId());
                waitSaveNotifyRecords.add(notifyRecord);
            });
            disinfectionNotifyRecordService.saveBatch(waitSaveNotifyRecords);
        } catch (Exception e) {
            log.error("任务创建通知异常", e);
        }

    }

    public void sendDisinfectionTaskExpireMessage(List<DisinfectionTask> tasks) {
        try {
            if (ListUtils.isEmpty(tasks)) {
                return;
            }
            List<DisinfectionNotifyRecord> notifyRecords = new ArrayList<>();
            MessageSendSceneQueryPram queryPram = MessageSendSceneQueryPram.builder()
                    .businessType(DISINFECTION_APPLICATION_CODE)
                    .sceneCodes(Collections.singletonList(DisinfectionNotifySceneCodeEnum.TASK_LAPSE_UNFINISHED.getCode()))
                    .build();
            List<MessageSendConfig> sendMsgConfigs = assemblyNoticeClient.getSendMsgConfig(queryPram);
            if (ListUtils.isEmpty(sendMsgConfigs)) {
                log.error("任务过期，没有找到对应通知模板");
                return;
            }
            //获取该场景下的通知记录
            List<DisinfectionNotifyRecord> records = disinfectionNotifyRecordService.listBySceneCode(DisinfectionNotifySceneCodeEnum.TASK_LAPSE_UNFINISHED);
            Map<Long, List<DisinfectionNotifyRecord>> disinfectionNotifyRecordMap = MapUtils.listGroupToMap(records, DisinfectionNotifyRecord::getBusinessId);
            //查询需要的人员姓名
            Map<Long, String> userNameMap = disinfectionUserHelper.getUserNameMap(ListUtils.convertItem(tasks, DisinfectionTask::getProjectLeaderId));
            tasks.forEach(task -> {
                Long projectLeaderId = task.getProjectLeaderId();

                String userName = userNameMap.getOrDefault(projectLeaderId, "");
                //发送消息
                sendMsgConfigs.forEach(sendMsgConfig -> {
                    //判断是否需要通知
                    if (isNotified(DisinfectionNotifySceneCodeEnum.TASK_LAPSE_UNFINISHED, sendMsgConfig, task.getId(), disinfectionNotifyRecordMap)) {
                        return;
                    }
                    Map<String, String> temParamMap = new HashMap<>();
                    temParamMap.put("leaderName", userName);
                    temParamMap.put("taskName", task.getTaskName());

                    MessageSendParam sendParam = MessageSendParam.builder()
                            .businessId(task.getId().toString())
                            .applicationCode(DISINFECTION_APPLICATION_CODE)
                            .notifyId(sendMsgConfig.getNotifyId())
                            .msgSceneCode(DisinfectionNotifySceneCodeEnum.TASK_LAPSE_UNFINISHED.getCode())
                            .notifyUsers(Collections.singleton(projectLeaderId))
                            .tempParams(temParamMap)
                            .build();
                    assemblyNoticeClient.sendMsg(sendParam);
                    DisinfectionNotifyRecord disinfectionNotifyRecord = new DisinfectionNotifyRecord();
                    disinfectionNotifyRecord.setNotifyUsers(projectLeaderId.toString());
                    disinfectionNotifyRecord.setTemplateId(sendMsgConfig.getNotifyId());
                    disinfectionNotifyRecord.setSceneCode(DisinfectionNotifySceneCodeEnum.TASK_LAPSE_UNFINISHED);
                    disinfectionNotifyRecord.setBusinessId(task.getId());
                    notifyRecords.add(disinfectionNotifyRecord);
                });
            });

            disinfectionNotifyRecordService.saveBatch(notifyRecords);
        } catch (Exception e) {
            log.error("任务过期通知失败", e);
        }

    }

    /**
     * 判断是否已经通知过
     */
    private boolean isNotified(DisinfectionNotifySceneCodeEnum sceneCodeEnum, MessageSendConfig sendMsgConfig,
                               long businessId, Map<Long, List<DisinfectionNotifyRecord>> notifyRecordMap
                              ) {
        List<DisinfectionNotifyRecord> notifyRecords = notifyRecordMap.get(businessId);
        //未查询到这个记录，说明没有通知过
        if (ListUtils.isEmpty(notifyRecords)) {
            return false;
        }
        for (DisinfectionNotifyRecord notifyRecord : notifyRecords) {
            Long templateId = notifyRecord.getTemplateId();
            DisinfectionNotifySceneCodeEnum sceneCode = notifyRecord.getSceneCode();
            if (sceneCodeEnum.equals(sceneCode) && templateId.equals(sendMsgConfig.getNotifyId())) {
                return true;
            }
        }
        return false;
    }

    public void sendDisinfectionTaskWillExpireMessage(Set<NotifyTaskDTO> willExpireTasks) {
        if (ListUtils.isEmpty(willExpireTasks)) {
            return;
        }
        MessageSendSceneQueryPram queryPram = MessageSendSceneQueryPram.builder()
                .businessType(DISINFECTION_APPLICATION_CODE)
                .sceneCodes(Collections.singletonList(DisinfectionNotifySceneCodeEnum.TASK_LAPSE.getCode()))
                .build();
        List<MessageSendConfig> sendMsgConfigs = assemblyNoticeClient.getSendMsgConfig(queryPram);
        if (ListUtils.isEmpty(sendMsgConfigs)) {
            log.error("任务即将过期，没有找到对应通知模板");
            return;
        }
        //获取该场景下的通知记录
        List<DisinfectionNotifyRecord> records = disinfectionNotifyRecordService.listBySceneCode(DisinfectionNotifySceneCodeEnum.TASK_LAPSE);
        Map<Long, List<DisinfectionNotifyRecord>> disinfectionNotifyRecordMap = MapUtils.listGroupToMap(records, DisinfectionNotifyRecord::getBusinessId);
        List<DisinfectionNotifyRecord> notifyRecords = new ArrayList<>();
        willExpireTasks.forEach(notifyTaskDTO -> {
            DisinfectionTask task = notifyTaskDTO.getDisinfectionTask();
            MessageSendConfig sendMsgConfig = notifyTaskDTO.getMessageSendConfig();
            //判读该任务下的该模板是否已通知
            if (isNotified(DisinfectionNotifySceneCodeEnum.TASK_LAPSE, sendMsgConfig, task.getId(), disinfectionNotifyRecordMap)) {

                return;
            }
            Long executorId = task.getExecutorId();
            if (Objects.isNull(executorId)) {
                return;
            }
            Map<String, String> temParamMap = new HashMap<>();
            temParamMap.put("duration", sendMsgConfig.getValue().toString());
            temParamMap.put("taskName", task.getTaskName());
            MessageSendParam sendParam = MessageSendParam.builder()
                    .businessId(task.getId().toString())
                    .applicationCode(DISINFECTION_APPLICATION_CODE)
                    .notifyId(sendMsgConfig.getNotifyId())
                    .msgSceneCode(DisinfectionNotifySceneCodeEnum.TASK_LAPSE.getCode())
                    .notifyUsers(Collections.singleton(executorId))
                    .tempParams(temParamMap)
                    .build();
            assemblyNoticeClient.sendMsg(sendParam);
            DisinfectionNotifyRecord disinfectionNotifyRecord = new DisinfectionNotifyRecord();
            disinfectionNotifyRecord.setBusinessId(task.getId());
            disinfectionNotifyRecord.setTemplateId(sendMsgConfig.getNotifyId());
            disinfectionNotifyRecord.setSceneCode(DisinfectionNotifySceneCodeEnum.TASK_LAPSE);
            disinfectionNotifyRecord.setNotifyUsers(executorId.toString());

            notifyRecords.add(disinfectionNotifyRecord);
        });

        disinfectionNotifyRecordService.batchSaveRecord(notifyRecords);
    }

    public List<MessageSendConfig> getConfigs(DisinfectionNotifySceneCodeEnum disinfectionNotifySceneCodeEnum) {
        MessageSendSceneQueryPram queryPram = MessageSendSceneQueryPram.builder()
                .businessType(DISINFECTION_APPLICATION_CODE)
                .sceneCodes(Collections.singletonList(disinfectionNotifySceneCodeEnum.getCode()))
                .build();
        return assemblyNoticeClient.getSendMsgConfig(queryPram);
    }
}
