package com.guhecloud.cube.srv.gen.loose.works.filter;

import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.database.annotation.DataPermissionField;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/8/13 17:27
 * @Description:
 */

@Component
public class ProjectScheduleDeptFieldParamConvert implements DataPermissionField.FieldParamConvert {
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
            return customDeptIds;
        }
        return null;
    }
}
