package com.guhecloud.cube.srv.prepaid.server.result;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 水表分页返回对象
 */
@Data
public class WaterMeterPageResult {
    private Long id;
    /**
     * 水表编号
     */
    private String meterId;
    /**
     * 商铺编号
     */
    private String roomId;
    /**
     * 剩余水量
     */
    private String account;
    /**
     * 总购水金额
     */
    private BigDecimal totalMoney;
    /**
     * 累计购水量
     */
    private String totalPower;
    /**
     * 水价
     */
    private BigDecimal price;
    /**
     * 是否失联
     */
    private Boolean unConnect;
    /**
     * 是否欠费
     */
    private Boolean oweStatus;
}
