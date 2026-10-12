package com.guhecloud.cube.srv.gen.energymanagement.server.queryparam;

import com.guhecloud.platform.base.model.Period;
import lombok.Data;

@Data
public class EnergyRingRatioQueryParam {
    private String instrumentType;
    private Period period;
    /**
     * 环比类型 TimePeriodQueryEnum
     */
    private String timeType;
}
