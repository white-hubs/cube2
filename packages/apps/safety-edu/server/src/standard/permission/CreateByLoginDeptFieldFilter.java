package com.guhecloud.cube.srv.gen.safety.education.server.standard.permission;

import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.database.annotation.DataPermissionField;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 创建人当前登陆人部门id处理
 */
@Component
public class CreateByLoginDeptFieldFilter implements DataPermissionField.FieldFilter {

    @Override
    public Object getFieldFilterParams() {
        //当前用户主键
        Long userId = SecurityContextHolder.getPrincipal().getId();
        UserClient userClient = ApplicationContextHolder.getBean(UserClient.class);
        UserDTO user = userClient.getUser(userId);
        if (!CollectionUtils.isEmpty(user.getOrganizations())) {
            List<Long> userIdList = userClient.listEnableUserByOrgAndPost(Collections
                                                                                  .singletonList(user.getOrganizations()
                                                                                                         .get(user.getOrganizations()
                                                                                                                      .size() - 1)
                                                                                                         .getId()), new ArrayList<>());
            if (CollectionUtils.isEmpty(userIdList)) {
                return null;
            }
            List<UserDTO> userDTOS = userClient.listUserByIds(userIdList);
            return userDTOS.stream().map(UserDTO::getUsername).collect(Collectors.toSet());
        }
        return null;
    }
}
