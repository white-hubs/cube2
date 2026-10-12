package com.guhecloud.cube.base.device.center.visualization.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class AreaEquipmentStatisticsVo {
    //离线
    private int offlineNum;
    //离线率
    private BigDecimal offlineRate = BigDecimal.ZERO;
    private List<DeviceAreaGroupVo> deviceAreaGroupVos;

}
