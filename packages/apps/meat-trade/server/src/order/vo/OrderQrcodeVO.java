package com.guhecloud.cube.biz.meattrade.server.order.vo;

import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 付款码信息
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderQrcodeVO {

    /**
     * 订单号
     */
    private Long orderId;

    /**
     * 二维码类型
     */
    private String type;

    /**
     * 价格
     */
    @Decimal
    private BigDecimal price;

    /**
     * 付款码
     */
    private String qrcode;

}