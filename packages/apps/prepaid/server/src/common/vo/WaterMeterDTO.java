package com.guhecloud.cube.srv.prepaid.common.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WaterMeterDTO {
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
    private String oweStatus;
}
