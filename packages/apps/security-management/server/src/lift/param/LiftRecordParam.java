package com.guhecloud.cube.srv.gen.securitymanagement.server.lift.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

@Data
public class LiftRecordParam extends Paging {
    private String transportWay;
    private String areaId;
    private Period period;
    private String area;
    private String keyword;
}
