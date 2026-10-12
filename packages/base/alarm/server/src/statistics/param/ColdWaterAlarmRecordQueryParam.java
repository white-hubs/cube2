package com.guhecloud.cube.base.alarm.server.statistics.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

import java.util.List;

@Data
public class ColdWaterAlarmRecordQueryParam extends Paging {

    private Period period;

    /**
     * 应用编号
     */
    private String applicationCode;

    /**
     * 设备主键集合
     */
    private List<Long> deviceIds;
}
