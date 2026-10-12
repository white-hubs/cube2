package com.guhecloud.cube.base.flow.flowable.definition.domain;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.flow.flowable.common.resovler.UserIdExpressionParser;
import com.guhecloud.cube.base.flow.flowable.common.resovler.UserNotificationChannelMappingDTO;
import com.guhecloud.cube.base.flow.flowable.definition.domain.entity.Copy;
import com.guhecloud.cube.base.flow.flowable.definition.domain.valueobject.CopyUser;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import lombok.Getter;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.runtime.ProcessInstance;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author pengjian
 * @date 2022/9/6 16:46
 * @description
 */
public class CopyNode {
    private final UserTaskNode taskNode;
    private Copy copy;
    @Getter
    private final String copyUserExpression;

    public CopyNode(UserTaskNode taskNode, String copyUserExpression) {
        this.taskNode = taskNode;
        this.copyUserExpression = copyUserExpression;
    }

    public Set<CopyUser> getCopyUsers(ProcessInstance processInstance) {
        if (Objects.isNull(copy)) {
            Set<CopyUser> copyUsers = new HashSet<>();
            UserIdExpressionParser userIdExpressionParser = ApplicationContextHolder.getBean(UserIdExpressionParser.class);
            List<UserNotificationChannelMappingDTO> userNotificationChannelMappingDTOS = userIdExpressionParser.resolveUsersAndNotify((ExecutionEntity) processInstance, this.copyUserExpression);
            userNotificationChannelMappingDTOS.forEach(userNotificationChannelMappingDTO -> {
                Set<String> userIds = userNotificationChannelMappingDTO.getUserIds();
                Set<ChannelEnum> channel = userNotificationChannelMappingDTO.getChannel();
                userIds.forEach(userId -> copyUsers.add(new CopyUser(Long.parseLong(userId), channel)));
            });
            copy.addCopyUsers(copyUsers);
        }
        return copy.getCopyUsers();
    }

}
