package com.guhecloud.cube.biz.meattrade.server.order.param;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author lzy
 * @date 2022/1/21
 */
@Data
public class OrderCommissionModifyParam {

    /**
     * 订单详情信息
     */
    @NotEmpty(message = "订单详情信息不能为空")
    private List<OrderDetail> orderDetails;

    /**
     * 市场租户编号
     */
    private Integer tenant;

    /**
     * 订单详情
     */
    @Data
    public static class OrderDetail {

        /**
         * 批发商主键
         */
        private Long supplierId;

        /**
         * 理货片数详情主键
         */
        private Long porkArrangeDetailId;

        /**
         * 重量
         */
        private BigDecimal weight;

        /**
         * 价格
         */
        private BigDecimal price;

        /**
         * 佣金
         */
        private BigDecimal commission;
    }

}
