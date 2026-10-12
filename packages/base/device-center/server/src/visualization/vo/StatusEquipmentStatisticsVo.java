package com.guhecloud.cube.base.device.center.visualization.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class StatusEquipmentStatisticsVo {
    //设备数
    private int deviceTotal;
    //环比
    private BigDecimal rate;
    //状态数组
    private List<DeviceStatusGroupVo> deviceStatusGroupVos;
}
