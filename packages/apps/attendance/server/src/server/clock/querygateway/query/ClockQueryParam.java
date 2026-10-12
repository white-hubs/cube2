package com.guhecloud.cube.srv.gen.attendance.management.server.clock.querygateway.query;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

@Data
public class ClockQueryParam extends Paging {

    private String keyword;

    private String userDeptId;

    private Period period;

    private Long attendanceGroupId;

    private Long arrangeClassesId;

    private String clockDate;

}
