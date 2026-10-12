package com.guhecloud.cube.biz.daily.task.param;

import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.model.PeriodTypeEnum;
import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/11/21 09:37
 * @Description:
 */
@Data
public class DailyBaseStatisticsParam {

    /**
     * 任务类型
     */
    private String dailyType;
    /**
     * 时间范围
     */
    private Period period;

    /**
     * 时间范围类型
     */
    private PeriodTypeEnum type;
}
