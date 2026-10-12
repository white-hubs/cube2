package com.guhecloud.cube.base.alarm.server.analysis.param;

import com.guhecloud.platform.base.model.Period;
import lombok.Data;

@Data
public class SafetyAnalysisAlarmParam {

    /**
     * 时间类型 TODAY:'今日',THIS_WEEK:'本周', THIS_MONTH:'本月',THIS_YEAR:'今年'
     */
    private String type;

    /**
     * 时间区间
     */
    private Period period;

    /**
     * 应用编码
     */
    private String applicationCode;

    private String filterStatus;
}
