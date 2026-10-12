package com.guhecloud.cube.srv.gen.official.management.server.querygateway.query;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

@Data
public class FlowStatParam extends Paging {
    private Long id;
    private String name;
    private Period period;
    private Long deptId;
    private String applicantName;
}
