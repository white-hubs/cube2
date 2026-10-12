package com.guhecloud.cube.biz.mdata.server.merchant.param;

import com.guhecloud.cube.biz.mdata.server.merchant.enumeration.MerchantEnum;
import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * 商户认证编辑
 */
@Data
public class AuthenticationUpdateCreate {
    /**
     * 主键
     */
    @Min(value = 1, message = "商户主键不能小于1")
    @NotNull(message = "商户主键不能为空")
    private Long id;
    /**
     * 认证状态
     */
    @NotNull(message = "认证状态不能为空")
    private MerchantEnum auditStatus;
}
