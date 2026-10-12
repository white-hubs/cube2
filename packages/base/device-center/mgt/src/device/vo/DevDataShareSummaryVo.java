package com.guhecloud.cube.iot.mgt.device.vo;

import lombok.Data;

import java.util.List;

@Data
public class DevDataShareSummaryVo {
    /**
     * 设备ID
     */
    private Long deviceId;

    /**
     * 作为源的共享配置列表
     */
    private List<DevDataShareVo> asSourceList;

    /**
     * 作为目标的共享配置列表
     */
    private List<DevDataShareVo> asTargetList;

    /**
     * 作为源的配置数量
     */
    private Integer asSourceCount;

    /**
     * 作为目标的配置数量
     */
    private Integer asTargetCount;
}

