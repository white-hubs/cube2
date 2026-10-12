package com.guhecloud.cube.srv.gen.personnel.management.server.filter;

import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.database.annotation.DataPermissionField;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/8/14 13:36
 * @Description:
 */

@Component
public class StaffDeptFieldFilter implements DataPermissionField.FieldFilter {
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
