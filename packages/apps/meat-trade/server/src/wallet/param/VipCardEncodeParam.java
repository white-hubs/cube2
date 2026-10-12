package com.guhecloud.cube.biz.meattrade.server.wallet.param;

import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 会员卡编码参数
 *
 * @author Woody
 * @date 2021/9/16
 */
@Data
public class VipCardEncodeParam {
    /**
     * 商户主键
     */
    @NotNull(message = "商户主键不能为空")
    @Min(value = 1, message = "商户主键不能小于1")
    private Long supplierId;

    /**
     * 会员卡号
     */
    @NotEmpty(message = "会员卡号不能为空")
    @Size(max = 100, message = "会员卡号长度不能大于100")
    private String code;
}
