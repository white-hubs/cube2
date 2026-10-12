package com.guhecloud.cube.base.device.center.visualization.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DeviceAreaGroupVo {
    private Long areaLastId;
    private String areaName;
    private int onLineNum;
    private int offlineNum;
    private BigDecimal offlineRate;
}
