package com.guhecloud.cube.customization.xialing.server.liangcang.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** 粮仓与智慧仓库维度的人员权限绑定（一仓可绑定多条人员）。 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("warehouse_person_permission")
public class WarehousePersonPermission extends BasicEnhancedTenantEntity<Long> {

    private Long id;
    /** 智慧仓库粮仓 ID。 */
    private Long warehouseId;
    /** 人员 ID。 */
    private Long personId;
}
