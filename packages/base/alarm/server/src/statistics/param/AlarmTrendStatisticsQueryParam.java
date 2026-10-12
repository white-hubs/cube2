package com.guhecloud.cube.base.alarm.server.statistics.param;

import com.guhecloud.platform.base.model.Period;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * 报警趋势统计参数
 *
 * @author lanchun
 * @date 2022/7/12 14:40
 */
@Data
public class AlarmTrendStatisticsQueryParam {

    //时间类型 DAY:'日',MONTH:'月',WEEK:'周',HOUR:'小时',YEAR:'年',TODAY:'今日',THIS_WEEK:'本周',
    // THIS_MONTH:'本月',RECENT_THREE_MONTH:'近三月',RECENT_HALF_YEAR:'近半年',THIS_YEAR:'今年'
    private String type;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 时间范围
     */
    @NotNull(message = "时间范围不能为空")
    private Period period;

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
     * 设备主键集合
     */
    private List<Long> deviceIds;

    /**
     * 应用编号
     */
    private String applicationCode;

    private Long areaId;
}
