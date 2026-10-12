package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.aggregate;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttClockDevicePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttClockLocationPO;
import lombok.Data;

import java.util.List;

@Data
public class AttClockRulesAggregate {
    private Long id;
    private String name;
    /**
     * 打卡方式
     */
    private String ways;
    /**
     * 是否可以外勤
     */
    private Boolean outClock;
    /**
     * 是否可以人脸识别
     */
    private Boolean faceRecognition;
    private List<AttClockDevicePO> deviceList;
    private List<AttClockLocationPO> locationList;
}
