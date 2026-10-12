package com.guhecloud.cube.srv.gen.securitymanagement.server.vehicleflowmonitoring.vo;

import lombok.Data;

@Data
public class VehicleFlowMonitoringTimeVo {
    private String direction;
    private int num;
    private String timeFlag;
    private String lane;
    private int inNum;
    private int outNum;
    private int speed;
}
