package com.guhecloud.cube.srv.gen.maintain.server.fault.filter;

import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.database.annotation.DataPermissionField;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/8/19 11:48
 * @Description:
 */
@Component
public class FaultOrderUserFieldFilter implements DataPermissionField.FieldFilter {
    @Override
    public Object getFieldFilterParams() {
        //当前用户主键
        Long userId = SecurityContextHolder.getPrincipal().getId();
        UserClient userClient = ApplicationContextHolder.getBean(UserClient.class);
        UserDTO user = userClient.getUser(userId);
        if (Objects.nonNull(user)) {
            return user.getMobile();
        }
        return null;
    }
}
