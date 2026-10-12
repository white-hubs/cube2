package com.guhecloud.cube.srv.gen.personnel.management.server.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/12/5 19:02
 * @Description: 员工类型
 */
@Data
@TableName("staff_type")
@EqualsAndHashCode(callSuper = false)
public class StaffTypePO extends BasicEnhancedTenantEntity<Long> implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 类型名称
     */
    private String name;

    /**
     * 类型名称
     */
    private String code;

    /**
     * 备注
     */
    private String remark;
}