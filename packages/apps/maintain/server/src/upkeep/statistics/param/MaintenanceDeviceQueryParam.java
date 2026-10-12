package com.guhecloud.cube.srv.gen.maintain.server.upkeep.statistics.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 设备维保统计查询参数
 *
 * @author lanchun
 * @date 2025/4/15 19:31
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MaintenanceDeviceQueryParam extends Paging {

    /**
     * 关键字
     */
    private String keyword;

    /**
     * 品类编号
     */
    private String categoryCode;

    /**
     * 时间范围条件
     */
    private Period period;
}
