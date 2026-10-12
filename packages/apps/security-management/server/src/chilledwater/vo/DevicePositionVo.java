package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DevicePositionVo {
    //设备id
    private Long deviceId;
    //设备名称
    private String deviceName;
    //设备编码
    private String deviceCode;
    //档位
    private String gearPositionName;
    //档位区域
    private String gearPositionArea;
    //档位编号
    private String gearPositionCode;
    //档位编号
    private String gearPositionId;
    //单价
    private BigDecimal unitPrice;
    //设定温度
    private String emplaceTemperature;
    //冷媒体单位流量
    private String unitTraffic;
}
