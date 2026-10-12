package com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RecordTimeVo {
    //设备名称
    private String deviceName;
    //设备Id
    private String deviceId;
    //记录时间
    private LocalDateTime recordTime;
}
