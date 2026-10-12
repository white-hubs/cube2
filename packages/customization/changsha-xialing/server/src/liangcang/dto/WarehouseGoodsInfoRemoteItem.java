package com.guhecloud.cube.customization.xialing.server.liangcang.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

import java.math.BigDecimal;

/** warehouseGoodsInfo 单条。 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WarehouseGoodsInfoRemoteItem {
    private JsonNode acquisitionDate;
    private BigDecimal volumeValue;
    private BigDecimal weightValue;
    private BigDecimal volumeDifference;
    private BigDecimal weightDifference;
}
