package com.guhecloud.cube.customization.xialing.server.liangcang.param;

import com.guhecloud.platform.base.model.Period;
import lombok.Data;

import java.util.List;

/** 粮仓温度趋势预警记录分页。 */
@Data
public class WarehouseTemperatureAlertRecordPageParam {
    /** 触发时间区间。 */
    private Period period;
    /** 智慧仓 id 列表，支持多选筛选。 */
    private List<Long> warehouseIds;
    /** 状态列表：ACTIVE / CLEARED。 */
    private List<String> statuses;
    /** 页码。 */
    private Long pageNumber;
    /** 每页数量。 */
    private Long pageSize;
}
