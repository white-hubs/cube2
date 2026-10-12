package com.guhecloud.cube.srv.gen.property.management.server.property.entity.assembly;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

/**
 * 资产管理员
 *
 * @author : lijie
 * @date : 2023-3-20
 */
@Data
@TableName("property_check_task_admin")
public class PropertyCheckTaskAdmin extends BasicEnhancedTenantEntity<Long> {
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 部门id
     */
    private Long orgId;
    /**
     * 资产管理员id
     */
    private Long adminId;
}
