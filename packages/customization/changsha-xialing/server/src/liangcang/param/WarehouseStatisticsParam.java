package com.guhecloud.cube.customization.xialing.server.liangcang.param;

import com.guhecloud.platform.base.model.Period;
import lombok.Data;

/**
 * 仓统计查询（GET Query 或等价入参）。
 */
@Data
public class WarehouseStatisticsParam {
    /** 聚合维度：年 / 季 / 月 / 日。 */
    private WarehouseStatDimension dimension;
    /** 统计时间闭区间（含 begin、end）。 */
    private Period period;
    /**
     * 指定单一智慧仓时只统计该仓；不传则统计当前已绑定的全部仓（可再按 {@link #warehouseCategory} 过滤）。
     * 日维下需要「按次周转」等列表时须指定。
     */
    private Long warehouseId;
    /** 与智慧仓 categoryName 一致的种类过滤，可空。 */
    private String warehouseCategory;
}
