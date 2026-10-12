package com.guhecloud.cube.base.device.center.visualization.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DeviceCountVo {
    private int deviceTotal;
    private int deviceTotalOld;
    private BigDecimal rate;
}
