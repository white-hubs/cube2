package com.guhecloud.cube.customization.xialing.server.liangcang.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/** 货位卡仓位档案。 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("warehouse_location_card_position_profile")
public class WarehouseLocationCardPositionProfile extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /** 粮仓 id。 */
    private Long warehouseId;
    /** 货位编号。 */
    private String positionCode;
    /** 平房仓号。 */
    private String flatWarehouseNo;
    /** 长度，单位 m。 */
    private BigDecimal length;
    /** 宽度，单位 m。 */
    private BigDecimal width;
    /** 高度，单位 m。 */
    private BigDecimal height;
    /** 温度阈值，单位 ℃。 */
    private BigDecimal temperatureThreshold;
    /** 温度范围最小值，单位 ℃。 */
    private BigDecimal temperatureRangeMin;
    /** 温度范围最大值，单位 ℃。 */
    private BigDecimal temperatureRangeMax;
    /** 数字粮体积，单位 m³。 */
    private BigDecimal digitalGrainVolume;
    /** 扣除体积，单位 m³。 */
    private BigDecimal deductedVolume;
    /** 包存量包数。 */
    private Integer storageBagCount;
    /** 平均包量。 */
    private BigDecimal averageBagWeight;
    /** 产地。 */
    private String originPlace;
    /** 收获年度。 */
    private String harvestYear;
    /** 保管员。 */
    private String keeper;
    /** 填卡日期。 */
    private String filingDate;
    /** 授权单位。 */
    private String authorizedUnit;
    /** 储存库点。 */
    private String storageSite;
    /** 入仓时间。 */
    private String inboundTime;
    /** 储存性质。 */
    private String storageNature;
    /** 保管方式。 */
    private String storageMode;
    /** 恒温率指标。 */
    private BigDecimal constantTemperatureRate;
    /** 容重/粗糙率。 */
    private BigDecimal bulkDensityOrRoughness;
    /** 折合水分。 */
    private BigDecimal convertedMoisture;
    /** 整精米粒/不完善粒。 */
    private BigDecimal wholeOrImperfectGrain;
}
