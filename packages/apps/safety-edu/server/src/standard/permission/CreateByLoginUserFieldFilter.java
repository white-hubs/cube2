package com.guhecloud.cube.srv.gen.safety.education.server.standard.permission;

import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.database.annotation.DataPermissionField;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * 创建人当前登陆人id处理
 */
@Component
public class CreateByLoginUserFieldFilter implements DataPermissionField.FieldFilter {

    @Override
    public Object getFieldFilterParams() {
        //当前用户主键
        Long userId = SecurityContextHolder.getPrincipal().getId();
        UserClient userClient = ApplicationContextHolder.getBean(UserClient.class);
        UserDTO user = userClient.getUser(userId);
        if (user != null) {
            return user.getUsername();
        }
        return null;
    }
}
