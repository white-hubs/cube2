package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.filter;

import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.database.annotation.DataPermissionField;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class RiskCurrentUserDeptFieldFilter implements DataPermissionField.FieldFilter {
    @Override
    public Object getFieldFilterParams() {
        Long userId = SecurityContextHolder.getPrincipal().getId();
        UserClient userClient = ApplicationContextHolder.getBean(UserClient.class);
        UserDTO user = userClient.getUser(userId);
        if (user == null || CollectionUtils.isEmpty(user.getOrganizations())) {
            return null;
        }
        List<Long> organizationIds = user.getOrganizations().stream()
                .map(UserDTO.Organization::getId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        if (CollectionUtils.isEmpty(organizationIds)) {
            return null;
        }
        return userClient.listEnableUserByOrgAndPost(organizationIds, new ArrayList<>());
    }
}
