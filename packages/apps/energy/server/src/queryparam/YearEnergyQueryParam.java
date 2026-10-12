package com.guhecloud.cube.srv.gen.energymanagement.server.queryparam;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 年用能查询对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class YearEnergyQueryParam extends Paging {
    private Long id;
    /**
     * 年份
     */
    private String year;
    /**
     * 能耗类型
     */
    private String instrumentType;

    /**
     * 时间区间
     */
    private Period period;
}
