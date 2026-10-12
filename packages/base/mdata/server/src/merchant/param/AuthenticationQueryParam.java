package com.guhecloud.cube.biz.mdata.server.merchant.param;

import com.guhecloud.cube.biz.mdata.server.merchant.enumeration.MerchantEnum;
import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.Size;

/**
 * 商品认证查询信息
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AuthenticationQueryParam extends Paging {
    /**
     * 商户名称
     */
    @Size(max = 20, message = "商户名称不能大于20")
    private String keyword;
    /**
     * 经营类型
     */
    private MerchantEnum type;
    /**
     * 业务类型
     */
    private MerchantEnum bizType;
    /**
     * 实名审核状态
     */
    private MerchantEnum auditStatus;
    /**
     * 卖家审核状态
     */
    private MerchantEnum sellerStatus;
}
