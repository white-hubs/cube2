package com.guhecloud.cube.customization.xialing.server.liangcang.param;

import com.guhecloud.platform.base.model.Period;
import lombok.Data;

/**
 * 外部粮仓时间范围查询参数。
 */
@Data
public class ExternalWarehousePeriodParam {
    /** 查询时间闭区间。 */
    private Period period;
    /** 可选；指定后只查询该绑定粮仓。 */
    private Long warehouseId;
}
