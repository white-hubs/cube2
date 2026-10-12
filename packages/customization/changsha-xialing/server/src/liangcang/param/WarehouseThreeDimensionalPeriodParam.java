package com.guhecloud.cube.customization.xialing.server.liangcang.param;

import com.guhecloud.platform.base.model.Period;
import lombok.Data;

/** 粮仓三维统计时间查询参数。 */
@Data
public class WarehouseThreeDimensionalPeriodParam {
    /** 查询时间闭区间。 */
    private Period period;
    /** 环境历史时间分片粒度，单位分钟；为空默认 1。 */
    private Integer stepMinutes;
    /** 环境历史聚合类型：raw、avg、min、max、count、sum、any；为空默认 raw。 */
    private String aggType;
}
