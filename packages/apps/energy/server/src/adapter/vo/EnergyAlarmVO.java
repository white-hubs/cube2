package com.guhecloud.cube.srv.gen.energymanagement.server.adapter.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EnergyAlarmVO {
    private String id;
    private String eventName;
    private LocalDateTime eventTime;
    private String eventType;
    private String eventLevel;
    private String eventStatus;//0未处理  1已处理
    private String eventJudge;//是否误报 0误报 1不是误报
    private String deviceId;
    private String handler;
    private LocalDateTime handleTime;
    private String remark;
    private LocalDateTime createTime;
}
