package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result;

import lombok.Data;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2023-09-28  09:31
 * @Description:
 * @Version: 1.0
 */
@Data
public class ClockInInfoClassesResult {
    /**
     * 班次id
     */
    private Long id;
    /**
     * 班次名称
     */
    private String name;

    /**
     * 上班时间
     */
    private String startWorkTime;
    /**
     * 下班时间
     */
    private String endWorkTime;
    /**
     * 打卡信息
     */
    private List<ClockInRecordsResult> records;
}
