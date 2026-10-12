package com.guhecloud.cube.srv.gen.energymanagement.server.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RouteDeviceVo {

    //设备名称
    private String deviceName;
    //设备id
    private String deviceId;
    //租户
    private Integer tenant;
    //设备类型
    private String instrumentType;

    private String sequence;

    private String routeId;

    private String routeName;

    private String deviceTypeId;

    //分摊比例
    private BigDecimal shareNum;
}
