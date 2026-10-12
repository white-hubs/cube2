package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 统计结果中「时间桶」一行（对桶内多笔货量先聚合再展示）。
 */
@Data
public class WarehouseStatisticsRowVO {
    /** 时间维标签，与入参 dimension 一致（如日、月、年、年-Q1）。 */
    private String timeDimension;
    private Long warehouseId;
    private String warehouseName;
    /** 仓型/品类。 */
    private String type;
    private String level;
    private Double warehouseHeight;
    /** 桶内空仓容积平均。 */
    private BigDecimal averageEmptyWarehouseVolume;
    /** 桶内货量体积（体积累计）平均。 */
    private BigDecimal averageInventory;
    /** 桶内货量重量平均。 */
    private BigDecimal averageInventoryWeight;
    /** 桶内周转率（空仓/货量）平均。 */
    private BigDecimal turnoverRate;
}
