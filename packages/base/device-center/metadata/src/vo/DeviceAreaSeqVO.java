package com.guhecloud.cube.base.device.center.metadata.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 区域内设备数量统计
 */
@Data
public class DeviceAreaSeqVO {
    private String areaName;
    private String areaId;
    private Integer num;
    private BigDecimal proportion;//这又是干嘛的
}
