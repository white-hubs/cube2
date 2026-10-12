package com.guhecloud.cube.biz.mdata.server.merchant.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商户档位关联信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "merchant_stall")
public class MerchantStall {

    /**
     * 主键
     */
    private Long id;

    /**
     * 商户主键
     */
    private Long merchantId;

    /**
     * 档位主键
     */
    private Long stallId;

    public MerchantStall(Long merchantId, Long stallId) {
        this.merchantId = merchantId;
        this.stallId = stallId;
    }
}
