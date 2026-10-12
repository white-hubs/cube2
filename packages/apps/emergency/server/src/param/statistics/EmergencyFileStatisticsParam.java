package com.guhecloud.cube.biz.emergency.server.param.statistics;

import com.guhecloud.platform.base.model.Period;
import lombok.Data;

@Data
public class EmergencyFileStatisticsParam {

    /**
     * 时间范围
     */
    private Period period;
}
