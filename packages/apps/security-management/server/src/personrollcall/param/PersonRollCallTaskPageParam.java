package com.guhecloud.cube.srv.gen.securitymanagement.server.personrollcall.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

@Data
public class PersonRollCallTaskPageParam extends Paging {

    /**
     * 点名日期
     */
    private Period callTimePeriod;

    /**
     * 点名类别
     */
    private String callType;

    /**
     * 点名任务
     */
    private String taskName;
}
