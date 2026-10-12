package com.guhecloud.cube.customization.xialing.server.liangcang.param;

/**
 * 统计时间维度，与「时间桶」划分方式一致。
 */
public enum WarehouseStatDimension {
    /** 按自然年。 */
    YEAR,
    /** 按季。 */
    QUARTER,
    /** 按自然月。 */
    MONTH,
    /** 按自然日。 */
    DAY,
}
