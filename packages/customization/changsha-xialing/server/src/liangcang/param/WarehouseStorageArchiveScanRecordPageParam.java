package com.guhecloud.cube.customization.xialing.server.liangcang.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** 储量档案扫描记录分页查询。 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WarehouseStorageArchiveScanRecordPageParam extends Paging {

    /** 智慧仓 id。 */
    private Long warehouseId;

    /** 扫描时间区间。 */
    private Period period;

    /** 状态：NORMAL / ALARM。 */
    private String status;
}
