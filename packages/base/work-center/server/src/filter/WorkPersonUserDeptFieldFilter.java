package com.guhecloud.cube.biz.work.center.server.filter;

import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.database.annotation.DataPermissionField;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/11/6 16:55
 * @Description:
 */
@Component
public class WorkPersonUserDeptFieldFilter implements DataPermissionField.FieldFilter {
    @Override
    public Object getFieldFilterParams() {
        //当前用户主键
        Long userId = SecurityContextHolder.getPrincipal().getId();
        UserClient userClient = ApplicationContextHolder.getBean(UserClient.class);
        UserDTO user = userClient.getUser(userId);
        if (!CollectionUtils.isEmpty(user.getOrganizations())) {
            return userClient.listEnableUserByOrgAndPost(user.getOrganizations().stream()
                                                                 .map(UserDTO.Organization::getId)
                                                                 .distinct()
                                                                 .collect(Collectors.toList()), new ArrayList<>());
        }
        return null;
    }

}
