package com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

@Data
public class WorkOvertimeLedgerQueryParam extends Paging {
    private String keyword;
    /**
     * 加班类型
     */
    private String workType;
    /**
     * 用户id
     */
    private Long userId;
    private String userName;
    /**
     * 部门id
     */
    private Long orgId;
    /**
     * 加班日期
     */
    private Period period;

}
