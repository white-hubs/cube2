package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** 气体采集记录。 */
@Data
public class GasArchiveRecordVO {

    private String id;

    private Long warehouseId;

    private LocalDateTime acquisitionTime;

    private BigDecimal co2;

    private BigDecimal o2;

    private BigDecimal ph3;

    private Boolean co2Alarm;

    private Boolean o2Alarm;

    private Boolean ph3Alarm;
}
