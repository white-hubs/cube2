package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.filter;

import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.annotation.DataPermissionField;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/5/22 11:55
 * @Description:
 */
@Component
public class InspectPlanUserDeptFieldFilter implements DataPermissionField.FieldFilter {

    @Override
    public Object getFieldFilterParams() {
        //当前用户主键
        Long userId = SecurityContextHolder.getPrincipal().getId();
        UserClient userClient = ApplicationContextHolder.getBean(UserClient.class);
        UserDTO user = userClient.getUser(userId);
        if (ObjectUtils.isNotEmpty(user) && ObjectUtils.isNotEmpty(user.getOrganization())) {
            List<Long> userIdList = userClient.listEnableUserByOrgAndPost(Collections.singletonList(user.getOrganization()
                                                                                                            .getId()), new ArrayList<>());
            if (CollectionUtils.isEmpty(userIdList)) {
                return null;
            }
            List<UserDTO> userDTOS = userClient.listUserByIds(userIdList);
            if (CollectionUtils.isEmpty(userDTOS)) {
                return null;
            }
            return userDTOS.stream().map(UserDTO::getUsername).collect(Collectors.toSet());
        }
        return null;
    }
}
