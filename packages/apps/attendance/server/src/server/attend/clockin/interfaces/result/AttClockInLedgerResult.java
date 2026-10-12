package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result;

import lombok.Data;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2023-09-26  13:48
 * @Description:
 * @Version: 1.0
 */
@Data
public class AttClockInLedgerResult {
    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    private String userName;
    /**
     * 部门id
     */
    private Long orgId;
    private String orgName;
    /**
     * 考勤组id
     */
    private Long attGroupId;
    private String attGroupName;
    /**
     * 统计时间
     */
    private String ledgerDate;
    /**
     * 考勤信息
     */
    private List<AttClockInLedgerDetailsResult> details;
}
