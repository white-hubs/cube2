package com.guhecloud.cube.srv.gen.official.management.server.querygateway.query;

import com.guhecloud.cube.srv.gen.official.management.server.enumeration.FocusWorkStatus;
import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

@Data
public class FocusWorkParam extends Paging {
    private FocusWorkStatus status;
    private String deptId;
    private String firstUserId;
    private Period period;
    private String keyword;
    private String mergeReferUser;
}
