package com.guhecloud.cube.biz.mdata.server.merchant.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.biz.mdata.server.merchant.enumeration.MerchantEnum;
import com.guhecloud.cube.biz.mdata.server.merchant.enumeration.RisklevelEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商户信息
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "merchant")
public class Merchant extends BasicEnhancedTenantEntity<Long> {
    /**
     * 主键
     */
    private Long id;
    /**
     * 用户主键
     */
    private Long userId;
    /**
     * 商户编号
     */
    private String code;
    /**
     * 商户名称
     */
    private String name;
    /**
     * 用户主键
     */
    private Long owner;
    /**
     * 卖家
     */
    private Boolean seller;
    /**
     * 买家
     */
    private Boolean buyer;
    /**
     * 联系人姓名
     */
    private String liaison;
    /**
     * 手机号码
     */
    private String phone;
    /**
     * 区域
     */
    private String area;
    /**
     * 详细地址
     */
    private String address;
    /**
     * 审核状态
     */
    private MerchantEnum status;
    /**
     * 是否启用
     */
    private Boolean enabled;
    /**
     * 备注说明
     */
    private String remark;
    /**
     * 风险级别
     */
    private RisklevelEnum riskLevel;
}
