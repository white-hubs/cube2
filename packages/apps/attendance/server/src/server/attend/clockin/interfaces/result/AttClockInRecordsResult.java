package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: lijie
 * @CreateTime: 2023-09-22  11:14
 * @Description:
 * @Version: 1.0
 */
@Data
public class AttClockInRecordsResult {
    /**
     * 打卡记录id
     */
    private Long id;
    /**
     * 班次id
     */
    private Long classesId;
    /**
     * 班次版本
     */
    private Integer classesVersion;
    /**
     * 打卡类型（上班：start、下班：end，更新打卡：revise）
     */
    private String clockType;
    /**
     * 打卡记录标识
     */
    private String uuid;

    /**
     * 来源
     */
    private String source;

    /**
     * 打卡时间
     */
    private String clockInTime;
    /**
     * 实际打卡时间
     */
    private String actualClockInTime;
    /**
     * 打卡状态（未打卡、正常、早退、迟到，缺卡、无效）
     */
    private String state;
    /**
     * 迟到、早退时间（分钟）
     */
    private Integer deletionDuration;
    /**
     * 加班时长
     */
    private Integer overtimeDuration;
    /**
     * 班次时间（年月日）
     */
    private LocalDateTime clockInDate;

}
