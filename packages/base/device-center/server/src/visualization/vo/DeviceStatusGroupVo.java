package com.guhecloud.cube.base.device.center.visualization.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DeviceStatusGroupVo {
    //状态
    private String deviceStatus;
    //数量
    private int num;
    //占比
    private BigDecimal proportion;
}
