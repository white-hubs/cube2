package com.guhecloud.cube.base.flow.context.enumeration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.base.flow.client.dto.TaskVariableDTO;
import com.guhecloud.cube.base.flow.flowable.common.message.NotifyUser;
import com.guhecloud.cube.base.flow.flowable.process.domain.ExtProcessInstance;
import com.guhecloud.cube.base.flow.flowable.task.interfaces.dto.ApprovalReminderDTO;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.UserTenantClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/8/8 13:56
 * @Description: 超时审批类型
 *
 */
public enum NotifyTypeEnum {

    /**
     * 当前审批人
     */
    currentApproval {
        @Override
        public List<NotifyUser> getNotifyTypeUser(TaskVariableDTO variableDTO, ApprovalReminderDTO config,
                                                  ExtProcessInstance extProcessInstance
                                                 ) {
            return Collections.singletonList(new NotifyUser(variableDTO.getAssignee()));
        }
    },

    /**
     * 指定人员
     */
    DESIGNATED_PERSONNEL {
        @Override
        public List<NotifyUser> getNotifyTypeUser(TaskVariableDTO variableDTO, ApprovalReminderDTO config,
                                                  ExtProcessInstance extProcessInstance

                                                 ) {
            if (CollectionUtils.isEmpty(config.getNotifyUsers())) {
                return new ArrayList<>();
            }
            return config.getNotifyUsers().stream().map(t -> new NotifyUser(t.getId())).collect(Collectors.toList());
        }
    },

    /**
     * 角色
     */
    ROLE {
        @Override
        public List<NotifyUser> getNotifyTypeUser(TaskVariableDTO variableDTO, ApprovalReminderDTO config,
                                                  ExtProcessInstance extProcessInstance
                                                 ) {
            if (CollectionUtils.isEmpty(config.getNotifyUsers())) {
                return new ArrayList<>();
            }
            UserClient userClient = ApplicationContextHolder.getBean(UserClient.class);
            List<UserDTO> userDTOS = userClient.listUsersByRoles(config.getNotifyUsers().stream()
                                                                         .map(ApprovalReminderDTO.NotifyInfo::getId)
                                                                         .collect(Collectors.toSet()));
            if (CollectionUtils.isNotEmpty(userDTOS)) {
                return userDTOS.stream().map(userDTO -> new NotifyUser(userDTO.getId())).collect(Collectors.toList());
            }
            return new ArrayList<>();
        }
    },

    /**
     * 岗位 + 发起人所属部门
     */
    APPOINTMENT {
        @Override
        public List<NotifyUser> getNotifyTypeUser(TaskVariableDTO variableDTO, ApprovalReminderDTO config,
                                                  ExtProcessInstance extProcessInstance
                                                 ) {
            if (CollectionUtils.isEmpty(config.getNotifyUsers())) {
                return new ArrayList<>();
            }
            UserClient userClient = ApplicationContextHolder.getBean(UserClient.class);
            UserDTO user = userClient.getUser(extProcessInstance.getStartUserInfo().getStartUserId());
            if (Objects.isNull(user)) {
                return new ArrayList<>();
            }
            UserTenantClient userTenantClient = ApplicationContextHolder.getBean(UserTenantClient.class);
            Set<Long> userIds = userTenantClient.getDeptListByPost(user.getOrganization().getId(), config.getNotifyUsers().stream()
                    .map(ApprovalReminderDTO.NotifyInfo::getId).collect(Collectors.toSet()));
            if (CollectionUtils.isNotEmpty(userIds)) {
                return userIds.stream().map(NotifyUser::new).collect(Collectors.toList());
            }
            return new ArrayList<>();
        }
    },

    /**
     * 职位 + 管理部门
     */
    POST {
        @Override
        public List<NotifyUser> getNotifyTypeUser(TaskVariableDTO variableDTO, ApprovalReminderDTO config,
                                                  ExtProcessInstance extProcessInstance
                                                 ) {
            if (CollectionUtils.isEmpty(config.getNotifyUsers())) {
                return new ArrayList<>();
            }
            // 通知人是职位；查询哪些用户的管理部门涉及到发起人所属部门
            UserClient userClient = ApplicationContextHolder.getBean(UserClient.class);
            UserDTO user = userClient.getUser(extProcessInstance.getStartUserInfo().getStartUserId());
            if (Objects.isNull(user)) {
                return new ArrayList<>();
            }
            UserTenantClient userTenantClient = ApplicationContextHolder.getBean(UserTenantClient.class);
            // 获取发起人信息
            Set<Long> userIds = userTenantClient.getManagedDeptList(user.getOrganization().getId(), config.getNotifyUsers().stream()
                    .map(ApprovalReminderDTO.NotifyInfo::getId).collect(Collectors.toSet()));
            if (CollectionUtils.isNotEmpty(userIds)) {
                return userIds.stream().map(NotifyUser::new).collect(Collectors.toList());
            }
            return new ArrayList<>();
        }
    },

    /**
     * 表单内人员
     */
    FORM_USER {
        @Override
        public List<NotifyUser> getNotifyTypeUser(TaskVariableDTO variableDTO, ApprovalReminderDTO config,
                                                  ExtProcessInstance extProcessInstance
                                                 ) {
            List<NotifyUser> notifyUsers = new ArrayList<>();
            UserClient userClient = ApplicationContextHolder.getBean(UserClient.class);
            if (StringUtils.isNotEmpty(config.getFormUser())) {
                List<Map<String, Object>> list = new ArrayList<>();
                Map<String, Object> variableMap = JsonUtils.deserialize(extProcessInstance.getData(), new TypeReference<Map<String, Object>>() {});
                Object variable = variableMap.get(config.getFormUser());
                if (Objects.isNull(variable)) {
                    return new ArrayList<>();
                }
                try {
                    if (variable instanceof List) {
                        list = (List<Map<String, Object>>) variable;
                    } else {
                        //根据电话号码找到用户
                        UserDTO userDTO = userClient.getUserByMobile(variable.toString());
                        if (Objects.nonNull(userDTO)) {
                            notifyUsers.add(new NotifyUser(userDTO.getId()));
                        }
                    }
                } catch (Exception e) {
                    list = (List<Map<String, Object>>) variableMap.get(config.getFormUser());
                }
                if (CollectionUtils.isNotEmpty(list)) {
                    for (Map<String, Object> assigneeUser : list) {
                        if (Objects.isNull(assigneeUser.get("id"))) {
                            continue;
                        }
                        notifyUsers.add(new NotifyUser(Long.parseLong(assigneeUser.get("id").toString())));
                    }
                }
            }
            return notifyUsers;
        }
    };

    public abstract List<NotifyUser> getNotifyTypeUser(TaskVariableDTO variableDTO, ApprovalReminderDTO config,
                                                       ExtProcessInstance extProcessInstance
                                                      );
}
