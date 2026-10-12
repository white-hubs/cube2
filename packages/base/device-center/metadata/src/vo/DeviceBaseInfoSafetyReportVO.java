package com.guhecloud.cube.base.device.center.metadata.vo;

import lombok.Data;

import java.util.List;

@Data
public class DeviceBaseInfoSafetyReportVO {
    /**
     * 设备总数
     */
    private Long totalNuM;
    /**
     * 上期设备总数
     */
    private Long ringNum;
    /**
     * 同期设备总数
     */
    private Long sameNum;

    private List<DeviceBaseInfoAndNumberVO> deviceBaseInfoAndNumberVOS;
    /**
     * 消防安全下设备类型
     */
    private List<DeviceFireSafetyReportVO> deviceFireSafetyReportVOS;
}
