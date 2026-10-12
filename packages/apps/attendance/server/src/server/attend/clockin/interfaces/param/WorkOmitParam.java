package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2023-09-26  17:43
 * @Description:
 * @Version: 1.0
 */
@Data
public class WorkOmitParam extends Paging {
    /**
     * 日期
     */
    private Period period;
    /**
     * 人员姓名
     */
    private Long userId;
    private String userName;
    /**
     * 部门id
     */
    private String orgId;
    /**
     * 考勤组
     */
    private Long groupId;
}
