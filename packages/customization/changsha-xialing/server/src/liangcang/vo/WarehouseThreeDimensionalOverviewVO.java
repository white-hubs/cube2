package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import lombok.Data;

import java.math.BigDecimal;

/** 粮仓三维统计总览。 */
@Data
public class WarehouseThreeDimensionalOverviewVO {
    /** 粮仓 id。 */
    private Long warehouseId;
    /** 粮仓名称。 */
    private String warehouseName;
    /** 粮仓种类。 */
    private String categoryName;
    /** 粮仓等级。 */
    private String qualityGrade;
    /** 当前库存重量，单位 t。 */
    private BigDecimal inventoryWeight;
    /** 当前库存体积。 */
    private BigDecimal inventoryVolume;
    /** 当前仓库平均温度。 */
    private BigDecimal avgTemperature;
    /** 三方测量更新时间。 */
    private Long measureUpdateTime;
}
