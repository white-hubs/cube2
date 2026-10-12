package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.filter;

import com.guhecloud.cube.srv.gen.maintain.server.common.util.PermissionFilterParamUtils;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.database.annotation.DataPermissionField;
import org.springframework.stereotype.Component;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/11/28 20:35
 * @Description:
 */

@Component
public class InspectTaskFieldParamConvert implements DataPermissionField.FieldParamConvert {
    @Override
    public Object getFieldParamConvert(Object customParam) {

        return ApplicationContextHolder.getBean(PermissionFilterParamUtils.class).getPermissionFilterParamByCustomDept(customParam);
    }
}
