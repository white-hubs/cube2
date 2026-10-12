package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param;

import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.enumeration.LogTypeEnum;
import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

@Data
public class OperateLogParam extends Paging {
    private Period period;
    private String keyword;
    private Long deviceId;
    private LogTypeEnum logTypeEnum;
}
