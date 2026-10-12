package com.guhecloud.cube.iot.mgt.north.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class LinkageLogPageParam extends Paging {
    private String ruleNum;
    private Integer tenantId;
    private String startTs;
    private String endTs;
}
