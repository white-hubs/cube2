package com.guhecloud.cube.base.flow.flowable.branch;

import com.guhecloud.cube.base.flow.flowable.common.ProcessHelper;
import com.guhecloud.cube.base.flow.flowable.common.ProcessUserDTO;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.activiti.engine.delegate.DelegateExecution;
import com.guhecloud.platform.base.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author pengjian
 * @date 2022/11/1 15:51
 * @description 发起人的信息
 */
@Component("submitter")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SubmitterService {
    private final ProcessHelper processHelper;
    private final UserClient userClient;

    public Long getUserId(DelegateExecution execution) {
        String processInstanceId = execution.getProcessInstanceId();
        ProcessUserDTO processStartUser = processHelper.getProcessStartUser(processInstanceId);
        return Objects.nonNull(processStartUser) ? Long.parseLong(processStartUser.getId()) : null;
    }

    public List<Long> getRoleId(DelegateExecution execution) {
        String processInstanceId = execution.getProcessInstanceId();
        ProcessUserDTO processStartUser = processHelper.getProcessStartUser(processInstanceId);
        UserDTO user = userClient.getUser(Long.parseLong(processStartUser.getId()));
        List<UserDTO.Role> roles = user.getRoles();
        if (CollectionUtils.isNotEmpty(roles)) {
            return Collections.emptyList();
        }
        return roles.stream().map(UserDTO.Role::getId).collect(Collectors.toList());
    }

    public Long getDeptId(DelegateExecution execution) {
        String processInstanceId = execution.getProcessInstanceId();
        ProcessUserDTO processStartUser = processHelper.getProcessStartUser(processInstanceId);
        UserDTO user = userClient.getUser(Long.parseLong(processStartUser.getId()));

        return user.getOrganization().getId();
    }

    public List<Long> getJobId(DelegateExecution execution) {
        String processInstanceId = execution.getProcessInstanceId();
        ProcessUserDTO processStartUser = processHelper.getProcessStartUser(processInstanceId);
        UserDTO user = userClient.getUser(Long.parseLong(processStartUser.getId()));
        return user.getPositions().stream().map(UserDTO.Position::getId).collect(Collectors.toList());
    }
}
