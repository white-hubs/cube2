package com.guhecloud.cube.biz.mdata.server.merchant.vo;

import lombok.Data;

/**
 * 买家商户信息
 */
@Data
public class MerchantBuyerVO {
    /**
     * 主键
     */
    private Long id;
    /**
     * 商户名称
     */
    private String name;
    /**
     * 联系人
     */
    private String liaison;
    /**
     * 手机号码
     */
    private String phone;
}
