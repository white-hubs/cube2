package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AttClockRuleListResult {
    private Long id;
    /**
     * 规则名称
     */
    private String name;
    /**
     * 打卡方式
     */
    private String ways;
    /**
     * 是否可以外勤
     */
    private boolean outClock;
    /**
     * 是否可以人脸识别
     */
    private boolean faceRecognition;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}
