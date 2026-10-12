package com.guhecloud.cube.customized.youxianqu.park.management.server.common.vo.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 趋势查询父对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TrendQueryPageBaseParam extends Paging {
    /**
     * 时间段
     */
    private Period period;
    /**
     * 时间段类型(PeriodTypeEnum)
     */
    private String periodType;
    /**
     * 趋势时间格式化字符串
     */
    private String format;
}
