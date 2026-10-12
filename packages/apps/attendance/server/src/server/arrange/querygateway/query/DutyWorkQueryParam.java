package com.guhecloud.cube.srv.gen.attendance.management.server.arrange.querygateway.query;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

@Data
public class DutyWorkQueryParam extends Paging {

    private String keyword;

    private Long groupId;

    private String month;

    private Boolean isCurrentUser;

    private Period period;

    private Long organizationId;

}
