package com.guhecloud.cube.customization.xialing.server.liangcang.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** 气体采集记录分页查询参数。 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GasArchiveRecordPageParam extends Paging {

    private Long warehouseId;

    private Period period;
}
