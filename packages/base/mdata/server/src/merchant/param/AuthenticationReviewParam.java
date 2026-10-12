package com.guhecloud.cube.biz.mdata.server.merchant.param;

import com.guhecloud.cube.biz.mdata.server.merchant.enumeration.MerchantEnum;
import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 商品审核信息
 */
@Data
public class AuthenticationReviewParam {

    /**
     * 认证主键
     */
    @NotNull(message = "认证主键不能为空")
    @Min(value = 1, message = "认证主键不能小于1")
    private Long id;
    /**
     * 认证状态
     */
    private MerchantEnum auditStatus;

    /**
     * 卖家状态
     */
    private MerchantEnum sellerStatus;
    /**
     * 实名认证信息不通过原因
     */
    @Size(max = 200, message = "认证信息不通过原因不能大于200")
    private String auditReason;
    /**
     * 卖家认证信息不通过原因
     */
    @Size(max = 200, message = "卖家认证信息不通过原因不能大于200")
    private String sellerReason;
}
