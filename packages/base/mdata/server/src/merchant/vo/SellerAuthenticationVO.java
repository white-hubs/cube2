package com.guhecloud.cube.biz.mdata.server.merchant.vo;

import com.guhecloud.cube.biz.mdata.server.goods.entity.GoodsCategory;
import com.guhecloud.cube.biz.mdata.server.merchant.enumeration.MerchantEnum;
import lombok.Data;

import java.util.List;

/**
 * 卖家认证信息
 */
@Data
public class SellerAuthenticationVO {
    /**
     * 主键
     */
    private Long id;
    /**
     * 商户主键
     */
    private Long merchantId;
    /**
     * 商品品类
     */
    private String goodsCategory;

    /**
     * 商品品类集合
     */
    private List<GoodsCategory> goodsCategories;
    /**
     * 卖家状态
     */
    private MerchantEnum status;
    /**
     * 卖家认证不通过原因
     */
    private String reason;
}
