package com.guhecloud.cube.biz.meattrade.server.order.param;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * 订单结算参数
 *
 * @author lzy
 * @date 2021/11/20
 */
@Data
public class OrderSettleBatchParam {
    /**
     * 支付密码
     */
    @NotEmpty(message = "支付密码不能为空")
    @Size(max = 100, message = "支付密码不能大于100")
    private String password;

    /**
     * 结算信息
     */
    @NotEmpty(message = "结算信息不能为空")
    private List<OrderSettleBatchParam.OrderSettle> orderSettles;

    @Data
    public static class OrderSettle {
        /**
         * 结算主键
         */
        private Long settleId;

        /**
         * 商户主键
         */
        private Long supplierId;

    }

}
