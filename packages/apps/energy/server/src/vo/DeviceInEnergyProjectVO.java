package com.guhecloud.cube.srv.gen.energymanagement.server.vo;

import lombok.Data;

@Data
public class DeviceInEnergyProjectVO {
    private String deviceId;
    private String deviceName;
    private String code;//设备编码
    private String projectId;
    private String projectName;
}
