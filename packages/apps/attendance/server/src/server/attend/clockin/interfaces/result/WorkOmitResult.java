package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result;

import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2023-11-13  10:32
 * @Description:
 * @Version: 1.0
 */
@Data
public class WorkOmitResult {
    /**
     * 用户id
     */
    private Long userId;
    private String userName;
    /**
     * 部门id
     */
    private String orgId;
    private String orgName;
    /**
     * 考勤组id
     */
    private Long attGroupId;
    private String attGroupName;
    /**
     * 补卡次数
     */
    private Integer num;
}
