package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import lombok.Data;

import java.math.BigDecimal;

/** 绑定粮仓日储量实时值。 */
@Data
public class ExternalWarehouseDailyStorageValueVO {
    private String date;
    private Long warehouseId;
    private String warehouseName;
    /** 体积实时值。 */
    private BigDecimal volumeValue;
    /** 质量实时值，单位 t。 */
    private BigDecimal weightValue;
}
