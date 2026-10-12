
package com.guhecloud.cube.iot.mgt.north.param;


import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import java.util.List;

@Data
public class DeviceEventQueryParam extends Paging {
    private Long deviceId;
    private List<String> eventCodes;
    /**
     * yyyy-MM-dd HH:mm:ss
     */
    private Long startTs;
    /**
     * yyyy-MM-dd HH:mm:ss
     */
    private Long endTs;
    private String order = "desc";
    /**
     * 默认最大1000条,避免一次性查询过多数据
     */
    private Integer maxRecords = 1000;
    /**
     * 事件类型，默认为触发(即设备产生告警) notify
     */
    private String bizType = "touch";
}
