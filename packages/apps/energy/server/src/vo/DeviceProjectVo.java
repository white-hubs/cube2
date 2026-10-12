package com.guhecloud.cube.srv.gen.energymanagement.server.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DeviceProjectVo {
    private Long itemId;
    //分项名称
    private String projectName;
    //分项id
    private Long projectId;
    //设备名称
    private String deviceName;
    //设备id
    private Long deviceId;
    //租户
    private Integer tenant;
    //设备类型
    private String instrumentType;
    private String deviceTypeId;

    //分摊比例
    private BigDecimal shareNum;

}
