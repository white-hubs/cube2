package com.guhecloud.cube.base.alarm.server.statistics.param;

import com.guhecloud.platform.base.model.Period;
import lombok.Data;

import java.util.List;

@Data
public class AlarmTargetTypeStatisticsQueryParam {

    /**
     * 对象类型主键集合
     */
    private List<String> alarmTargetTypeId;

    /**
     * 时间范围
     */
    private Period period;
}
