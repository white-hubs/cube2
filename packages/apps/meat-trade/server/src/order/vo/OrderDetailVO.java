package com.guhecloud.cube.biz.meattrade.server.order.vo;

import com.guhecloud.cube.biz.meattrade.server.base.enumeration.MeatGoodsFreshLabelEnum;
import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author lzy
 * @date 2021/12/10
 */
@Data
public class OrderDetailVO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 订单主键
     */
    private Long orderId;

    /**
     * 批次号
     */
    private String porkReportCode;

    /**
     * 商品主键
     */
    private String goodsCategoryId;

    /**
     * 商品名称
     */
    private String goodsCategoryName;

    /**
     * 冷热鲜标识
     */
    private MeatGoodsFreshLabelEnum freshLabel;

    /**
     * 商品编码
     */
    private String label;

    /**
     * 脚标编码
     */
    private String footMarkCode;

    /**
     * 理货数据详情主键
     */
    private Long porkArrangeDetailId;

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
     * 食安溯源主键
     */
    private Long spotCheckResourceId;

    /**
     * 序号
     */
    private Integer sort;

    /**
     * 市场租户编号
     */
    private Integer tenant;

}
