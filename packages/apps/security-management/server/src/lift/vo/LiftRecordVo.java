package com.guhecloud.cube.srv.gen.securitymanagement.server.lift.vo;

import com.guhecloud.cube.srv.gen.securitymanagement.server.lift.enumeration.TransportWayEnum;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LiftRecordVo {
    private Long id;
    private String deviceId;
    private String deviceName;
    private TransportWayEnum transportWay;
    private String deviceType;
    private String areaName;
    private String areaId;
    private LocalDateTime runTime;
    private String floor;
}
