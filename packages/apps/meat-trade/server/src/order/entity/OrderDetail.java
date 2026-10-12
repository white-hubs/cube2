package com.guhecloud.cube.biz.meattrade.server.order.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.biz.meattrade.server.base.enumeration.MeatGoodsFreshLabelEnum;
import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 订单详情
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("meattrade_order_detail")
public class OrderDetail extends BasicEnhancedEntity<Long> {

    /**
     * 主键
     */
    private Long id;

    /**
     * 订单主键
     */
    private Long orderId;

    /**
     * 理货数据详情主键
     */
    private Long porkArrangeDetailId;

    /**
     * 商品品类主键
     */
    private String goodsCategoryId;

    /**
     * 商品品类名称
     */
    private String goodsCategoryName;

    /**
     * 冷热鲜标识
     */
    private MeatGoodsFreshLabelEnum freshLabel;

    /**
     * 商品主键
     */
    private Long goodsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 损耗
     */
    @Decimal
    private BigDecimal loss;

    /**
     * 重量
     */
    @Decimal
    private BigDecimal weight;

    /**
     * 单价
     */
    @Decimal
    private BigDecimal unitPrice;

    /**
     * 总价
     */
    @Decimal
    private BigDecimal price;

    /**
     * 佣金
     */
    @Decimal
    private BigDecimal commission;

    /**
     * 佣金率
     */
    @Decimal
    private BigDecimal commissionRatio;

    /**
     * 商品是否取消
     */
    private String checked;

    /**
     * 结算主键
     */
    private Long settlementId;

    /**
     * 市场租户编号
     */
    private Integer tenant;
}
