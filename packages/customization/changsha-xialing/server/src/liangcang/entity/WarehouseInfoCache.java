package com.guhecloud.cube.customization.xialing.server.liangcang.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** 三方粮仓信息缓存。 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("warehouse_info_cache")
public class WarehouseInfoCache extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long warehouseId;
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private Integer parentId;
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String name;
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String categoryName;
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private Double totalVolume;
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String qualityGrade;
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private Double warehouseHeight;
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private Boolean enabledScan;
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String scanCorn;
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private Double materialVolume;
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private Double materialWeight;
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private Double usageRate;
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String cameraSetting;
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String setting;
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String deviceListJson;
}
