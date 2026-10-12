package com.guhecloud.cube.base.flow.flowable.process.application.service;

import com.guhecloud.cube.base.flow.flowable.process.application.commandgateway.command.StartProcessInstanceCommand;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;

import java.util.Objects;

public class ProcessInstanceNameFactory {
    public static String getProcessInstanceName(StartProcessInstanceCommand param) {
        RepositoryService repositoryService = ApplicationContextHolder.getBean(RepositoryService.class);
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(param.getProcessDefinitionKey()).singleResult();
        UserDTO user = new UserDTO();
        if (param.isPublicForm()) {
            user.setName(param.getSubmitName());
        } else {
            Long userId = SecurityContextHolder.getPrincipal().getId();
            UserClient userClient = ApplicationContextHolder.getBean(UserClient.class);
            user = userClient.getUser(userId);
            if (Objects.isNull(user)) {
                user = new UserDTO();
                user.setName(SecurityContextHolder.getPrincipal().getName());
                user.setId(userId);
            }
        }
        return user.getName() + "发起的" + processDefinition.getName();
    }
}
