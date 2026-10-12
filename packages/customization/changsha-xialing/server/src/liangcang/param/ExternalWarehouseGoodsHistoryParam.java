package com.guhecloud.cube.customization.xialing.server.liangcang.param;

import com.guhecloud.platform.base.model.Period;
import lombok.Data;

/** 外部粮仓货物历史查询参数。 */
@Data
public class ExternalWarehouseGoodsHistoryParam {
    /** 可选；查询时间闭区间。 */
    private Period period;
    /** 可选；指定后只查询该粮仓。 */
    private Long warehouseId;
}
