package com.guhecloud.cube.biz.linkage.server.linkage.strategy.messagenofity.messagetarget.handler;

import com.guhecloud.apps.linkage.metadata.dto.MessageTargetNotifyDTO;
import com.guhecloud.apps.linkage.metadata.enumeration.MessageTargetNotifyEnum;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.messagenofity.messagetarget.MessageTargetAbstractHandler;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.messagenofity.messagetarget.dto.MessageDepartmentPostDTO;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.platform.base.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 部门岗位用户
 *
 * @author lanchun
 * @date 2022/10/31 16:49
 */
@Slf4j
@Component
public class DepartmentPostHandler extends MessageTargetAbstractHandler {

    @Autowired
    private UserClient userClient;

    public DepartmentPostHandler() {
        super(MessageTargetNotifyEnum.DEPARTMENT_POST);
    }

    @Override
    public List<UserDTO> getUsers(MessageTargetNotifyDTO messageTargetNotifyDTO) {
        if (Objects.isNull(messageTargetNotifyDTO.getTargetConfig())) {
            return Collections.emptyList();
        }
        MessageDepartmentPostDTO messageDepartmentPostDTO = JsonUtils.deserialize(messageTargetNotifyDTO.getTargetConfig(), MessageDepartmentPostDTO.class);
        if (Objects.isNull(messageDepartmentPostDTO)) {
            return Collections.emptyList();
        }
        List<UserDTO> userDTOS = this.userClient.organizationAndPostEnabledUsers(messageDepartmentPostDTO.getDepartmentId(), messageDepartmentPostDTO.getPostId());
        userDTOS.stream().forEach(userDTO -> {
            UserDTO.Organization organization = new UserDTO.Organization();
            organization.setId(messageDepartmentPostDTO.getDepartmentId());
            organization.setName(messageDepartmentPostDTO.getDepartmentName());
            userDTO.setOrganization(organization);
        });
        return userDTOS;
    }

    @Override
    public Set<Long> getTargetIds(MessageTargetNotifyDTO messageTargetNotifyDTO) {
        return Collections.emptySet();
    }
}
