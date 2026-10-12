package com.guhecloud.cube.biz.mdata.server.merchant.vo;

import com.guhecloud.cube.biz.mdata.server.merchant.enumeration.MerchantEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 商品认证信息
 */
@Data
public class AuthenticationPageVO {
    /**
     * 主键
     */
    private Long id;
    /**
     * 商户主键
     */
    private Long merchantId;
    /**
     * 经营性质
     */
    private MerchantEnum type;
    /**
     * 联系人
     */
    private String liaison;
    /**
     * 联系手机号
     */
    private String phone;
    /**
     * 实名认证状态
     */
    private MerchantEnum auditStatus;
    /**
     * 卖家认证状态
     */
    private MerchantEnum sellerStatus;
    /**
     * 审核时间
     */
    private LocalDateTime reviewTime;
    /**
     * 提交时间
     */
    private LocalDateTime submitTime;
    /**
     * 商户名称
     */
    private String merchantName;
}
