package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.filter;

import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.database.annotation.DataPermissionField;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Component
public class RiskCustomDeptFieldParamConvert implements DataPermissionField.FieldParamConvert {
    @Override
    public Object getFieldParamConvert(Object customParam) {
        Set<Long> departmentIds = new LinkedHashSet<>();
        collectDepartmentIds(customParam, departmentIds);
        if (CollectionUtils.isEmpty(departmentIds)) {
            return null;
        }
        UserClient userClient = ApplicationContextHolder.getBean(UserClient.class);
        return userClient.listEnableUserByOrgAndPost(new ArrayList<>(departmentIds), new ArrayList<>());
    }

    private void collectDepartmentIds(Object value, Set<Long> target) {
        if (value == null) {
            return;
        }
        if (value instanceof Iterable<?> iterable) {
            iterable.forEach(item -> collectDepartmentIds(item, target));
            return;
        }
        if (value instanceof Map<?, ?> map) {
            map.values().forEach(item -> collectDepartmentIds(item, target));
            return;
        }
        Long id = parseLong(value);
        if (Objects.nonNull(id)) {
            target.add(id);
        }
    }

    private Long parseLong(Object value) {
        try {
            return Long.valueOf(value.toString());
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
