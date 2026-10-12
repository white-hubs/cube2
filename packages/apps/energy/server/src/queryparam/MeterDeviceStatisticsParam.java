package com.guhecloud.cube.srv.gen.energymanagement.server.queryparam;

import com.guhecloud.cube.srv.gen.energymanagement.server.enumeration.QueryTimeUnitEnum;
import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

@Data
public class MeterDeviceStatisticsParam extends Paging {
    private String deviceId;
    private String instrumentType;
    private QueryTimeUnitEnum timeFlag;
    private Period period;
    private String propCode;
}
