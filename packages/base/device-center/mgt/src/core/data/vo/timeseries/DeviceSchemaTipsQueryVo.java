package com.guhecloud.cube.iot.mgt.core.data.vo.timeseries;

import com.guhecloud.cube.iot.mgt.core.def.TimeRangeAware;
import lombok.Data;

@Data
public class DeviceSchemaTipsQueryVo implements TimeRangeAware {
    private static final int DEFAULT_PAGE_SIZE = 10000;
    private long pageNumber = 1;
    private long pageSize = DEFAULT_PAGE_SIZE;

    private String tenantId;
    private String devNumLike;
    private String devNameLike;
    /**
     * 仅查询父级设备
     */
    private boolean onlyParent = false;
    /**
     * 指定父级设备编码
     */
    private String parentDevNum;

    private String startTime;
    private String endTime;
    /**
     * 默认最大1000条,避免一次性查询过多数据
     */
    private Integer maxRecords = DEFAULT_PAGE_SIZE;

}
