package com.guhecloud.cube.base.device.center.metadata.vo;

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
