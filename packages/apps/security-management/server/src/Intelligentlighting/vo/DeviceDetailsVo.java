package com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.vo;

import lombok.Data;

@Data
public class DeviceDetailsVo {
    private Long id;
    //对象类型
    private String objectType;
    //对象
    private String deviceObject;
    //区域
    private String areaName;
    //控制时间点
    private String controlTime;
    //开关值
    private String openGate;
    //光照度比例
    private Integer brightness;
    //时控id
    private Long timeControlId;
    //对象名称
    private String deviceName;

    private Integer sort;
}
