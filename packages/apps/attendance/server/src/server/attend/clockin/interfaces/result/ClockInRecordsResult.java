package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result;

import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2023-09-28  09:36
 * @Description:
 * @Version: 1.0
 */
@Data
public class ClockInRecordsResult {
    /**
     * 打卡类型
     */
    private String clockType;
    /**
     * 打卡状态
     */
    private String state;
    /**
     * 打卡时间
     */
    private String actualClockInTime;
}
