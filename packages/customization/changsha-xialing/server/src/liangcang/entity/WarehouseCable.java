package com.guhecloud.cube.customization.xialing.server.liangcang.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** 粮仓电缆。 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("warehouse_cable")
public class WarehouseCable extends BasicEnhancedTenantEntity<Long> {

    private Long id;
    /** 智慧仓库粮仓 ID。 */
    private Long warehouseId;
    /** 业务电缆编号。 */
    private String cableNo;
    /** 点位数量。 */
    private Integer pointCount;
}
