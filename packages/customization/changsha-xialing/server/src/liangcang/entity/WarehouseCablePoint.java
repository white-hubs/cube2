package com.guhecloud.cube.customization.xialing.server.liangcang.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** 粮仓电缆点位与设备映射。 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("warehouse_cable_point")
public class WarehouseCablePoint extends BasicEnhancedTenantEntity<Long> {

    private Long id;
    /** 智慧仓库粮仓 ID。 */
    private Long warehouseId;
    /** 电缆 ID。 */
    private Long cableId;
    /** 点位序号。 */
    private Integer pointNo;
    /** 映射的设备中心设备 ID。 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private Long deviceId;
}
