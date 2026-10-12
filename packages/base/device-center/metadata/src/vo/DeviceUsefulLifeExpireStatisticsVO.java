package com.guhecloud.cube.base.device.center.metadata.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DeviceUsefulLifeExpireStatisticsVO {

    /**
     * 设备总数
     */
    private Integer totalDeviceNumber;

    /**
     * 到期数
     */
    private Integer expireNum;

    /**
     * 到期率
     */
    private BigDecimal expireRate;
}
