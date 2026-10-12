package com.guhecloud.cube.base.device.center.visualization.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DeviceTypeGroupVo {
    //类型
    private String deviceType;
    private String deviceTypeId;
    //数量
    private int num;
    //占比
    private BigDecimal proportion = BigDecimal.ZERO;

}
