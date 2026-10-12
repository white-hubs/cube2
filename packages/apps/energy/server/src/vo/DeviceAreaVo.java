package com.guhecloud.cube.srv.gen.energymanagement.server.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DeviceAreaVo {
    private Long itemId;
    //区域名称
    private String areaName;
    //区域id
    private Long areaId;
    //设备名称
    private String deviceName;
    //设备id
    private Long deviceId;
    //租户
    private Integer tenant;
    //设备类型
    private String instrumentType;

    private String sequence;

    private String deviceTypeId;

    //分摊比例
    private BigDecimal shareNum;
}
