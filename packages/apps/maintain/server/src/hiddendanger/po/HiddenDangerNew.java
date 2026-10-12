package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.po;

import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.filter.HiddenDangerNewDeptFieldFilter;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.filter.HiddenDangerNewDeptFieldParamConvert;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.filter.HiddenDangerNewUserFieldFilter;
import com.guhecloud.platform.database.annotation.DataPermission;
import com.guhecloud.platform.database.annotation.DataPermissionField;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author wql
 * @describe 隐患单2.0
 * @date 2023/11/8 20:08
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@DataPermission({
        @DataPermissionField(permission = "CURRENT_USER", field = "create_by", fieldFilter = HiddenDangerNewUserFieldFilter.class),
        @DataPermissionField(permission = "CURRENT_USER_DEPT", field = "create_by", fieldFilter = HiddenDangerNewDeptFieldFilter.class),
        @DataPermissionField(permission = "CUSTOM_DEPT", field = "create_by",
                             fieldParamConvert = HiddenDangerNewDeptFieldParamConvert.class
        )
}
)
public class HiddenDangerNew extends BasicEnhancedTenantEntity<Long> {
    /**
     * 主键
     */
    private Long id;

    /**
     * 流程实例id
     */
    private String processInsId;

    /**
     * 隐患单号
     */
    private String code;

    /**
     * 隐患名称
     */
    private String name;

    /**
     * 隐患对象主键
     */
    private Long hiddenObjId;

    /**
     * 隐患对象名称
     */
    private String hiddenObjName;

    /**
     * 隐患对象类别
     */
    private String hiddenObjType;

    /**
     * 隐患类型id
     */
    private Long hiddenTypeId;

    /**
     * 隐患来源
     */
    private String source;

    /**
     * 隐患描述
     */
    private String remark;

    /**
     * 隐患程度
     */
    private Long urgencyLevel;

    /**
     * 处理结果
     */
    private String result;

    /**
     * 提交时间
     */
    private LocalDateTime submitTime;

    /**
     * 提交人
     */
    private String submitStaff;

    /**
     * 处理状态
     */
    private String state;

    /**
     * 是否响应
     */
    private Boolean response;
}
