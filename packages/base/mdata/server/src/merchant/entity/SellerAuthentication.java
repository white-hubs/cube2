package com.guhecloud.cube.biz.mdata.server.merchant.entity;

import com.guhecloud.cube.biz.mdata.server.merchant.enumeration.MerchantEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 卖家认证
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SellerAuthentication extends BasicEnhancedTenantEntity<Long> {
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
     * 卖家状态
     */
    private MerchantEnum status;
    /**
     * 卖家认证不通过原因
     */
    private String reason;
    /**
     * 提交时间
     */
    private LocalDateTime submitTime;
}
