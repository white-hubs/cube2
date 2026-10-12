package com.guhecloud.cube.biz.meattrade.server.wallet.param;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

/**
 * 会员卡解码参数
 *
 * @author Woody
 * @date 2021/9/16
 */
@Data
public class VipCardDecodeParam {
    /**
     * 会员卡密文
     */
    @NotEmpty(message = "会员卡密文不能为空")
    @Size(max = 100, message = "会员卡密文长度不能大于100")
    private String ciphertext;
}
