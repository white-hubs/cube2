package com.guhecloud.cube.base.device.center.visualization.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OverallEquipmentStatisticsVo {
    //设备数
    private int deviceTotal;
    //环比
    private BigDecimal rate;
    //类型数组
    private List<DeviceTypeGroupVo> deviceTypeGroup;
    //其他类型数组
    private List<DeviceTypeGroupVo> otherDeviceTypeGroup;
}
