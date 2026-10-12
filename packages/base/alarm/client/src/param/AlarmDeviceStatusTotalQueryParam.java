package com.guhecloud.cube.base.alarm.client.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import java.util.List;

@Data
public class AlarmDeviceStatusTotalQueryParam extends Paging {
    /**
     * 时间范围
     */
    private String period;

    /**
     * 设备id
     */
    private List<Long> ids;
    /**
     * 应用编号
     */
    private String applicationCode;
}
