package com.guhecloud.cube.iot.mgt.core.data.vo.timeseries;

import lombok.Data;

@Data
public class LinkageEventHistoryQueryVo {
    private Integer pageNumber;
    private Integer pageSize;
    private String ruleNum;
    private Integer tenantId;
    private String instanceId;
    private String startTs;
    private String endTs;
}
