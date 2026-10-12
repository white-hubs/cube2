package com.guhecloud.cube.biz.collection.analysis.gm.model.param;

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
     * 时间段查询类型（TimePeriodQueryEnum）
     */
    private String timePeriodType;
    /**
     * 趋势时间格式化字符串
     */
    private String format;
}
