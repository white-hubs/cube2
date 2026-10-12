package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.filter;

import com.guhecloud.cube.srv.gen.maintain.server.common.util.PermissionFilterParamUtils;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.database.annotation.DataPermissionField;
import org.springframework.stereotype.Component;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/11/28 20:31
 * @Description: 巡检任务数据权限转换
 */
@Component
public class InspectTaskFieldFilter implements DataPermissionField.FieldFilter {

    @Override
    public Object getFieldFilterParams() {
        return ApplicationContextHolder.getBean(PermissionFilterParamUtils.class).getPermissionFilterParamByUserDept();
    }
}
