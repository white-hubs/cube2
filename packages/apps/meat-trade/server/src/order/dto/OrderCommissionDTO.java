package com.guhecloud.cube.biz.meattrade.server.order.dto;

import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author lzy
 * @date 2022/1/21
 */
@Data
public class OrderCommissionDTO {

    /**
     * 批发商信息
     */
    private Long supplierId;

    /**
     * 佣金
     */
    @Decimal
    private BigDecimal commission;

    /**
     * 总量损失
     */
    @Decimal
    private BigDecimal weightLoss;

    /**
     * 订单详情信息
     */
    private List<OrderDetailDTO> orderDetails;

}
