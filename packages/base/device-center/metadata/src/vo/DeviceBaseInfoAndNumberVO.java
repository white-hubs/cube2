package com.guhecloud.cube.base.device.center.metadata.vo;

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
    private Integer totalNumber;

    /**
     * 在线数量
     */
    private Integer normalNumber;

    /**
     * 离线数量
     */
    private Integer offlineNumber;
}
