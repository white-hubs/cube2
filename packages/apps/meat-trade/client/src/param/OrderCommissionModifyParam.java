package com.guhecloud.cube.biz.meattrade.client.param;

import lombok.Data;

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
    private List<OrderCommissionModifyParam.OrderDetail> orderDetails;

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
         * 单价
         */
        private BigDecimal price;
    }

}
