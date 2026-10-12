package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttClockLocationPO;
import lombok.Data;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2023-10-10  17:45
 * @Description:
 * @Version: 1.0
 */
@Data
public class AttClockRuleResult {
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

    /**
     * 打卡地点
     */
    private List<AttClockLocationPO> location;
    private Integer tenant;
}
