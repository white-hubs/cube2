package com.guhecloud.cube.base.device.center.open.vo;

import lombok.Data;

@Data
public class DeviceFireSafetyReportVO {
    /**
     * 设备总数
     */
    private Long deviceNum;

    /**
     * 设备类别
     */
    private String categoryName;
}
