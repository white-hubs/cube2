package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import lombok.Data;

import java.math.BigDecimal;

/** 气体档案仓房当前信息。 */
@Data
public class GasArchiveBasicInfoVO {

    private Long warehouseId;

    private String warehouseName;

    private BigDecimal co2;

    private BigDecimal o2;

    private BigDecimal ph3;

    private Boolean co2Alarm;

    private Boolean o2Alarm;

    private Boolean ph3Alarm;
}
