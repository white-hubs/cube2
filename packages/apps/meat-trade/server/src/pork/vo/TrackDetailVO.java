package com.guhecloud.cube.biz.meattrade.server.pork.vo;

import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 供货商品跟进商品明细
 */
@Data
public class TrackDetailVO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 商户主键
     */
    private Long supplierId;

    /**
     * 报备号
     */
    private String porkReportCode;

    /**
     * 电子标签
     */
    private String label;

    /**
     * 脚标编码
     */
    private String footMarkCode;

    /**
     * 净重
     */
    @Decimal
    private BigDecimal weight;

    /**
     * 销售价格
     */
    @Decimal
    private BigDecimal price;

    /**
     * 理货地点
     */
    private String place;

    /**
     * 理货时间
     */
    private LocalDateTime porkArrangeTime;

    /**
     * 损耗
     */
    @Decimal
    private BigDecimal loss;

    /**
     * 交易金额
     */
    @Decimal
    private BigDecimal amount;

    /**
     * 交易时间
     */
    private LocalDateTime orderTime;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 佣金
     */
    private BigDecimal commission;

    /**
     * 是否合格
     */
    private Boolean passed;

    /**
     * 是否售卖
     */
    private Boolean sell;

    /**
     * 识别结果
     */
    private Boolean result;

    private Boolean cheat;

}