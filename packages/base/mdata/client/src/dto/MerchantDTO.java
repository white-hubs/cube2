package com.guhecloud.cube.biz.mdata.client.dto;

import com.guhecloud.cube.biz.mdata.client.enumeration.MerchantEnum;
import com.guhecloud.cube.biz.mdata.client.enumeration.RiskLevelEnum;
import lombok.Data;

/**
 * 商户资料信息
 */
@Data
public class MerchantDTO {
    /**
     * 主键
     */
    private Long id;
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
    private Long userId;
    /**
     * 用户主键
     */
    private Long owner;
    /**
     * 是否为卖家
     */
    private Boolean seller;
    /**
     * 是否为买家
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
     * 风险级别
     */
    private RiskLevelEnum riskLevel;
}
