package com.guhecloud.cube.srv.gen.maintain.server.common.filter;

import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.database.annotation.DataPermissionField;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import com.guhecloud.platform.base.util.CollectionUtils;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/6/25 16:37
 * @Description:
 */

@Component
public class MaintainCurrentUserDeptFieldFilter implements DataPermissionField.FieldFilter {
    @Override
    public Object getFieldFilterParams() {
        //当前用户主键
        Long userId = SecurityContextHolder.getPrincipal().getId();
        UserClient userClient = ApplicationContextHolder.getBean(UserClient.class);
        UserDTO user = userClient.getUser(userId);
        if (!CollectionUtils.isEmpty(user.getOrganizations())) {
            return user.getOrganizations().get(user.getOrganizations().size() - 1).getId();
        }
        return null;
    }
}