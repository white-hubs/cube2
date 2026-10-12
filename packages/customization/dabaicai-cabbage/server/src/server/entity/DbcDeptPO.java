package com.guhecloud.cube.srv.gen.cabbage.server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 大白菜部门
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("dbc_dept")
@EqualsAndHashCode(callSuper = true)
public class DbcDeptPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * 市场编号
     */
    private Integer marketId;
    //所属部门序号
    private String departmentCode;
    //所属部门名称
    private String departmentName;
    // 对应古河云部门Id(全路径部门 用逗号隔开)
    private String organizationIds;
}
