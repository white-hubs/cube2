package com.guhecloud.cube.base.device.center.open.vo;

import lombok.Data;

@Data
public class DeviceBaseInfoAndNumberVO {
    /**
     * 分组标签
     */
    private String tagCode;
    /**
     * 设备总数
     */
    private Long totalNumber;

    /**
     * 在线数量
     */
    private Long normalNumber;

    /**
     * 离线数量
     */
    private Long offlineNumber;
}
