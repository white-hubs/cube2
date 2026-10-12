package com.guhecloud.cube.biz.traffic.detection.server.detection.application.querygateway.query;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TrafficDetectionAreaQueryParam extends Paging {

    private String keyword;

    private Period period;
}
