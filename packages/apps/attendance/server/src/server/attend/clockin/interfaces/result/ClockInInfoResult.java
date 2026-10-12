package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result;

import lombok.Data;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2023-09-21  19:02
 * @Description:
 * @Version: 1.0
 */
@Data
public class ClockInInfoResult {
    /**
     * 当前班次信息
     */
    private List<AttClassesRecordResult> classesInfo;
    /**
     * 打卡规则
     */
    private AttClockRuleResult clockRule;
}
