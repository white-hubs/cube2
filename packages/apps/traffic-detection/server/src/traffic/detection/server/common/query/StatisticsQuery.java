package com.guhecloud.cube.biz.traffic.detection.server.common.query;

import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class StatisticsQuery extends Paging {

    //时间类型 DAY:'日',MONTH:'月',WEEK:'周',HOUR:'小时',YEAR:'年',TODAY:'今日',THIS_WEEK:'本周',
    // THIS_MONTH:'本月',RECENT_THREE_MONTH:'近三月',RECENT_HALF_YEAR:'近半年',THIS_YEAR:'今年'
    private String type;

    /**
     * mysql日期格式
     */
    private String dateExp;

    /**
     * 格式化格式
     */
    private String dateString;

    /**
     * 展示的日期格式
     */
    private String viewDateString;

    /**
     * 时间范围
     */
    private Period period;
}
