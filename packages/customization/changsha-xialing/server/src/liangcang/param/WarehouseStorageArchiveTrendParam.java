package com.guhecloud.cube.customization.xialing.server.liangcang.param;

import com.guhecloud.cube.customization.xialing.server.liangcang.enums.WarehouseStorageArchiveTrendTimeType;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

/** 储量档案变化曲线查询。 */
@Data
public class WarehouseStorageArchiveTrendParam {

    /** 智慧仓 id。 */
    private Long warehouseId;

    /** 时间范围，默认今日。 */
    private WarehouseStorageArchiveTrendTimeType timeType;

    /** 自定义日期范围。 */
    private Period period;
}
