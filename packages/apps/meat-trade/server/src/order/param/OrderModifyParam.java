package com.guhecloud.cube.biz.meattrade.server.order.param;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * 订单新增信息
 */
@Data
public class OrderModifyParam {

    /**
     * 批发商主键
     */
    private Long supplierId;

    /**
     * 订单商品详情
     */
    @NotNull(message = "订单商品详情不能为空")
    private List<OrderDetailModifyParam> details;
    /**
     * 买家主键
     */
    @NotNull(message = "买家不能为空")
    private Long customerId;
}
