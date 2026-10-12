package com.guhecloud.cube.srv.prepaid.server.result;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 抄表记录返回对象
 */
@Data
public class CollectRecordsResult {
    private Long id;
    /**
     * 电表编号
     */
    private String meterId;
    /**
     * 采集时间(对应第三方接口的创建时间createTime)
     */
    private LocalDateTime collectTime;
    /**
     * 剩余金额(元)
     */
    private BigDecimal powerRemain;
    /**
     * 总用电量
     */
    private String powerTatol;
    /**
     * 尖电能
     */
    private String powerHigher;
    /**
     * 峰电能
     */
    private String powerHigh;
    /**
     * 平电能
     */
    private String powerFlat;
    /**
     * 谷电能
     */
    private String powerLow;
    /**
     * 尖电价
     */
    private BigDecimal priceHigher;
    /**
     * 峰电价
     */
    private BigDecimal priceHigh;
    /**
     * 平电价
     */
    private BigDecimal priceFlat;
    /**
     * 谷电价
     */
    private BigDecimal priceLow;
}
