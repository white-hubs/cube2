package com.guhecloud.cube.srv.gen.attendance.management.server.filter;

import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.database.annotation.DataPermissionField;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Component
public class AttendanceCustomDepFieldFilter implements DataPermissionField.FieldParamConvert {
    @Override
    public Object getFieldParamConvert(Object param) {
        //当前用户主键
        if (Objects.isNull(param)) {
            return null;
        }
        //自定义部门角色过滤
        Set<Long> customDeptIds = new HashSet<>();
        if (param instanceof List) {
            ((List) param).stream().forEach(t -> {
                customDeptIds.add(Long.valueOf(t.toString()));
            });
        } else {
            customDeptIds.add(Long.valueOf(param.toString()));
        }
        if (!CollectionUtils.isEmpty(customDeptIds)) {
            UserClient userClient = ApplicationContextHolder.getBean(UserClient.class);
            List<Long> userIdList = userClient.listEnableUserByOrgAndPost(new ArrayList<>(customDeptIds), new ArrayList<>());
            if (CollectionUtils.isEmpty(userIdList)) {
                return null;
            }
            return userIdList;
        }
        return null;
    }
}
