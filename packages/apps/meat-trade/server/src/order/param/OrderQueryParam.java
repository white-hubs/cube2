package com.guhecloud.cube.biz.meattrade.server.order.param;

import com.guhecloud.cube.biz.meattrade.server.order.enumeration.OrderEnum;
import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

/**
 * @author lzy
 * @date 2021/12/8
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OrderQueryParam extends Paging {

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字不能大于100")
    private String keyword;

    /**
     * 批发商主键
     */
    private Long supplierId;

    /**
     * 客户主键
     */
    private Long customerId;

    /**
     * 批发商主键集合
     */
    private Collection<? extends Serializable> supplierIds;

    /**
     * 客户主键集合
     */
    private Collection<? extends Serializable> customerIds;

    /**
     * 订单状态
     */
    private OrderEnum status;

    /**
     * 时间
     */
    private Period period;

    /**
     * 市场租户编号
     */
    private Integer tenant;

    /**
     * 订单详情信息
     */
    private List<OrderDetail> orderDetails;

    /**
     * 订单详情
     */
    @Data
    public static class OrderDetail {
        /**
         * 理货片数详情主键
         */
        private Long porkArrangeDetailId;

        /**
         * 重量
         */
        private BigDecimal weight;
    }

}
