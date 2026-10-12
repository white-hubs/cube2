package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.querygateway.query;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CollectionQuery extends Paging {
    /**
     * 入职时间
     */
    private Period timeOfInduction;

    /**
     * 员工姓名
     */
    private String keyword;

    /**
     * 部门id
     */
    private Long organizationId;

    private Long attendanceGroupId;

}
