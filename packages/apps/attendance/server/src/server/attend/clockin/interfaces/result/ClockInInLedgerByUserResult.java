package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result;

import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2023-10-13  15:49
 * @Description:
 * @Version: 1.0
 */
@Data
public class ClockInInLedgerByUserResult {
    /**
     * 今天多少号
     */
    private String today;
    /**
     * 考勤是否正常
     */
    private String normal = "NOT_CHECKED";
    /**
     * 是否有班次
     */
    private Boolean classes = false;
    /**
     * 是否有审批
     */
    private Boolean apply = false;
}
