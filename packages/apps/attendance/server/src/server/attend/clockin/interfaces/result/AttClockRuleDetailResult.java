package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AttClockRuleDetailResult {
    private Long id;
    /**
     * 名称
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
     * 编辑时间
     */
    private LocalDateTime updateTime;
    /**
     * 打卡地点
     */
    private List<AttClockLocationResult> locationList;
    /**
     * 打卡设备
     */
    private List<AttDeviceResult> deviceList;
}
