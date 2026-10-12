package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DeviceAssociationPositionParam {
    //冷冻水设备
    private Long coldWaterDevice;
    //设备档位
    private Long gearPositionId;
    //单价
    private BigDecimal unitPrice;
    //设定温度
    private String emplaceTemperature;
    //冷媒体单位流量
    private String unitTraffic;
    //档位
    private String gearPositionName;
    //档位编号
    private String gearPositionCode;
    //档位区域
    private String gearPositionArea;
    //档位区域id
    private String gearPositionAreaId;
}
