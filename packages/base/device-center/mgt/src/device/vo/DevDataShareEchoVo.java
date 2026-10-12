package com.guhecloud.cube.iot.mgt.device.vo;

import lombok.Data;

import java.util.List;

/**
 * 设备数据共享信息回显（分页/详情）。
 */
@Data
public class DevDataShareEchoVo {
    /**
     * 作为数据源的共享配置列表（设备详情回显）
     */
    private List<DevDataShareVo> dataShareAsSourceList;

    /**
     * 作为数据目标的共享配置列表（设备详情回显）
     */
    private List<DevDataShareVo> dataShareAsTargetList;

    /**
     * 作为数据源的共享配置数量（设备分页回显）
     */
    private Integer dataShareAsSourceCount;

    /**
     * 作为数据目标的共享配置数量（设备分页回显）
     */
    private Integer dataShareAsTargetCount;
}
