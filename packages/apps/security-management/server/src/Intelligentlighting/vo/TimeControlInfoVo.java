package com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TimeControlInfoVo {
    private Long id;
    //计划名称
    private String planName;
    //时间周期
    private String timeCycle;
    //开始时间
    private LocalDateTime planStartTime;
    //计划结束时时间
    private LocalDateTime planEndTime;
    //是否启用
    private Boolean enabled;
    //回路数
    private Integer num;
    //设备
    private List<DeviceDetailsVo> deviceDetails;
}
