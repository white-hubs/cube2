package com.guhecloud.cube.biz.mdata.server.merchant.dto;

import com.guhecloud.cube.biz.mdata.server.merchant.entity.Merchant;
import com.guhecloud.cube.biz.mdata.server.merchant.enumeration.MerchantEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 商户认证信息
 */
@Data
public class AuthenticationDTO {
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
     * 姓名
     */
    private String name;
    /**
     * 身份证号
     */
    private String identityCard;
    /**
     * 业务角色
     */
    private MerchantEnum bizType;
    /**
     * 经营主体类型
     */
    private MerchantEnum subjectType;
    /**
     * 主体名称
     */
    private String subjectName;
    /**
     * 经营证照号
     */
    private String licenseNo;
    /**
     * 实名审核状态
     */
    private MerchantEnum auditStatus;
    /**
     * 卖家审核状态
     */
    private MerchantEnum sellerStatus;
    /**
     * 审核不通过原因
     */
    private String reason;
    /**
     * 审核时间
     */
    private LocalDateTime reviewTime;
    /**
     * 提交时间
     */
    private LocalDateTime submitTime;
    /**
     * 商户资料信息
     */
    private Merchant merchant;
}
