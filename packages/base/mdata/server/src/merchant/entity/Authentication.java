package com.guhecloud.cube.biz.mdata.server.merchant.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.biz.mdata.server.merchant.enumeration.MerchantEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 商户认证信息
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("authentication")
public class Authentication extends BasicEnhancedTenantEntity<Long> {
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
     * 经营主体类型
     */
    private String subjectType;
    /**
     * 主体名称
     */
    private String subjectName;
    /**
     * 经营证照号
     */
    private String licenseNo;
    /**
     * 审核状态
     */
    private MerchantEnum status;
    /**
     * 审核不通过原因
     */
    private String reason;
    /**
     * 提交时间
     */
    private LocalDateTime submitTime;
    /**
     * 审核时间
     */
    private LocalDateTime reviewTime;

}
