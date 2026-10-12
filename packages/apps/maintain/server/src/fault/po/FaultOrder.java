package com.guhecloud.cube.srv.gen.maintain.server.fault.po;

import com.guhecloud.cube.srv.gen.maintain.server.fault.filter.FaultOrderDeptFieldFilter;
import com.guhecloud.cube.srv.gen.maintain.server.fault.filter.FaultOrderDeptFieldParamConvert;
import com.guhecloud.cube.srv.gen.maintain.server.fault.filter.FaultOrderUserFieldFilter;
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
 * @describe 故障单2.0
 * @date 2023/11/6 9:21
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DataPermission({
        @DataPermissionField(permission = "CURRENT_USER", field = "create_by", fieldFilter = FaultOrderUserFieldFilter.class),
        @DataPermissionField(permission = "CURRENT_USER_DEPT", field = "create_by", fieldFilter = FaultOrderDeptFieldFilter.class),
        @DataPermissionField(permission = "CUSTOM_DEPT", field = "create_by", fieldParamConvert = FaultOrderDeptFieldParamConvert.class)
}
)
public class FaultOrder extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * 流程实例id
     */
    private String processInsId;
    /**
     * 故障单编号
     */
    private String code;
    /**
     * 故障单名称
     */
    private String name;
    /**
     * 故障对象id
     */
    private Long objId;
    /**
     * 故障对象名称
     */
    private String objName;
    /**
     * 故障对象类型
     */
    private String objType;
    /**
     * 网格id
     */
    private Long areaId;
    /**
     * 故障类型id
     */
    private Long faultTypeId;
    /**
     * 故障来源
     */
    private String source;
    /**
     * 紧急程度
     */
    private String urgencyLevel;
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
     * 状态
     */
    private String state;
    /**
     * 维护成本
     */
    private Double cost;

    /**
     * 故障描述
     */
    private String faultDescribe;
    private String applicationCode;

    /**
     * 处理进度
     */
    private String progress;
}
