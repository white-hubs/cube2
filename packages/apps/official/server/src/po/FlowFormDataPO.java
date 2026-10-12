package com.guhecloud.cube.srv.gen.official.management.server.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.official.management.server.filter.FlowFormDataDeptFieldFilter;
import com.guhecloud.cube.srv.gen.official.management.server.po.model.FlowCommonEntity;
import com.guhecloud.platform.database.annotation.DataPermission;
import com.guhecloud.platform.database.annotation.DataPermissionField;
import lombok.Data;

/**
 * @author appet
 * @date 2023/02/24
 * @description 通用流程数据表
 */
@Data
@TableName("flow_form_data")
@DataPermission({
        @DataPermissionField(permission = "CURRENT_USER", field = "applicant_id"),
        @DataPermissionField(permission = "CURRENT_USER_DEPT", field = "applicant_dept_id", fieldFilter = FlowFormDataDeptFieldFilter.class
        ),
        @DataPermissionField(permission = "CUSTOM_DEPT", field = "applicant_dept_id")
}
)
public class FlowFormDataPO extends FlowCommonEntity {

    private Long attendanceGroupId;//考情组ID

    private String businessType;//业务类型

    private String jsonForm;//表单的json字符串数据

    private String status;
}