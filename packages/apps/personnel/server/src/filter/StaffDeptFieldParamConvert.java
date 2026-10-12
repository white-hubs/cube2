package com.guhecloud.cube.srv.gen.personnel.management.server.filter;

import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.database.annotation.DataPermissionField;
import org.springframework.stereotype.Component;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/8/14 13:36
 * @Description:
 */

@Component
public class StaffDeptFieldParamConvert implements DataPermissionField.FieldParamConvert {
    @Override
    public Object getFieldParamConvert(Object customParam) {
        if (Objects.isNull(customParam)) {
            return null;
        }
        //自定义部门角色过滤
        Set<Long> customDeptIds = new HashSet<>();
        if (customParam instanceof List) {
            ((List) customParam).stream().forEach(t -> {
                customDeptIds.add(Long.valueOf(t.toString()));
            });
        } else {
            customDeptIds.add(Long.valueOf(customParam.toString()));
        }
        if (!CollectionUtils.isEmpty(customDeptIds)) {
            UserClient userClient = ApplicationContextHolder.getBean(UserClient.class);
            return userClient.listEnableUserByOrgAndPost(new ArrayList<>(customDeptIds), new ArrayList<>());
        }
        return null;
    }
}
