package com.guhecloud.cube.base.alarm.server.statistics.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

import java.util.List;

@Data
public class AlarmDeviceStatusTotalQueryParam extends Paging {
    /**
     * 时间
     */
    private Period period;
    //设备id
    private List<Long> ids;
    /**
     * 应用编号
     */
    private String applicationCode;
}
