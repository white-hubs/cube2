package com.guhecloud.cube.base.flow.flowable.resolver.listener;

import base.constant.VariableConstant;
import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.flow.flowable.definition.domain.valueobject.CopyUser;
import com.guhecloud.cube.base.flow.flowable.resolver.dto.PostInfo;
import com.guhecloud.cube.base.flow.flowable.resolver.dto.Properties;
import com.guhecloud.cube.base.flow.flowable.resolver.dto.RoleInfo;
import com.guhecloud.cube.base.flow.flowable.resolver.dto.UserInfo;
import com.guhecloud.cube.base.flow.flowable.resolver.enumeration.AssigneeTypeEnums;
import com.guhecloud.cube.base.flow.flowable.task.application.commandgatway.ActivatedTaskCommandService;
import com.guhecloud.cube.base.flow.flowable.task.application.commandgatway.command.CopyCommand;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.UserTenantClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.ServiceTask;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.impl.el.FixedValue;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author pengjian
 * @date 2022/11/7 17:13
 * @description
 */
@Component
public class ServiceListener implements JavaDelegate {
    /**
     * 自定义字段
     */
    private FixedValue copyUser;

    @Override
    public void execute(DelegateExecution execution) {
        long currentTimeMillis = System.currentTimeMillis();
        FlowElement currentFlowElement = execution.getCurrentFlowElement();
        ServiceTask serviceTask = (ServiceTask) currentFlowElement;
        String expressionText = copyUser.getExpressionText();
        Properties properties = JsonUtils.deserialize(expressionText, Properties.class);
        String startUserId = (String) execution.getVariable(VariableConstant.ROOT);
        List<UserInfo> assignedUsers = new ArrayList<>();
        if (properties.getAssignedType().equals(AssigneeTypeEnums.ASSIGN_USER.name())) {
            assignedUsers = properties.getAssignedUser().stream()
                    .map(userInfo -> JsonUtils.deserialize(JsonUtils.serialize(userInfo), UserInfo.class)).collect(Collectors.toList());
        } else if (properties.getAssignedType().equals(AssigneeTypeEnums.ROLE.name())) {
            List<RoleInfo> roleInfos = properties.getAssignedUser().stream()
                    .map(roleInfo -> JsonUtils.deserialize(JsonUtils.serialize(roleInfo), RoleInfo.class)).collect(Collectors.toList());
            // 获取角色下的用户
            UserClient userClient = ApplicationContextHolder.getBean(UserClient.class);
            List<UserDTO> userDTOS = userClient.listUsersByRoles(roleInfos.stream().map(RoleInfo::getId).map(Long::valueOf)
                                                                         .collect(Collectors.toSet()));
            if (CollectionUtils.isNotEmpty(userDTOS)) {
                for (UserDTO userDTO : userDTOS) {
                    if (!userDTO.getEnabled()) {
                        continue;
                    }
                    UserInfo userInfo = new UserInfo();
                    userInfo.setId(userDTO.getId().toString());
                    userInfo.setName(userDTO.getName());
                    assignedUsers.add(userInfo);
                }
            }
        } else if (AssigneeTypeEnums.FORM_USER.name().equals(properties.getAssignedType())) {
            String formUser = properties.getFormUser();
            List<Object> assigneeUsers = execution.getVariable(formUser, List.class);
            if (assigneeUsers != null) {
                for (Object assigneeUser : assigneeUsers) {
                    LinkedHashMap linkedHashMap = (LinkedHashMap) assigneeUser;
                    String id = String.valueOf(linkedHashMap.get("id"));
                    UserInfo userInfo = new UserInfo();
                    userInfo.setId(id);
                    assignedUsers.add(userInfo);
                }
            }
        } else if (AssigneeTypeEnums.APPOINTMENT.name().equals(properties.getAssignedType())) {
            UserClient userClient = ApplicationContextHolder.getBean(UserClient.class);
            UserDTO user = userClient.getUser(Long.parseLong(startUserId));
            List<PostInfo> postInfos = properties.getAssignedUser().stream()
                    .map(postInfo -> JsonUtils.deserialize(JsonUtils.serialize(postInfo), PostInfo.class)).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(postInfos)) {
                UserTenantClient userTenantClient = ApplicationContextHolder.getBean(UserTenantClient.class);
                Set<Long> userIds = userTenantClient.getDeptListByPost(user.getOrganization().getId(), postInfos.stream()
                        .map(postInfo -> Long.parseLong(postInfo.getId())).collect(Collectors.toSet()));
                List<UserDTO> users = userClient.listEnableUserByIds(userIds);
                if (CollectionUtils.isNotEmpty(users)) {
                    for (UserDTO userDTO : users) {
                        UserInfo userInfo = new UserInfo();
                        userInfo.setId(userDTO.getId().toString());
                        userInfo.setName(userDTO.getName());
                        assignedUsers.add(userInfo);
                    }
                }
            }
        }
        List<CopyUser> copyUsers = new ArrayList<>();
        Set<ChannelEnum> channelTypeEnums = new HashSet<>();
        Collections.addAll(channelTypeEnums, ChannelEnum.CHANNEL_SITE, ChannelEnum.CHANNEL_APP);
        assignedUsers.forEach(userInfo -> copyUsers.add(new CopyUser(Long.parseLong(userInfo.getId()), channelTypeEnums)));

        CopyCommand copyCommand = new CopyCommand(
                serviceTask.getId(),
                serviceTask.getName(),
                execution.getProcessInstanceId(),
                new HashSet<>(copyUsers)
        );
        ActivatedTaskCommandService activatedTaskCommandService = ApplicationContextHolder.getBean(ActivatedTaskCommandService.class);
        activatedTaskCommandService.copy(copyCommand);
        System.out.println("超送 execute time: " + (System.currentTimeMillis() - currentTimeMillis));
    }
}