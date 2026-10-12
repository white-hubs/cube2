package com.guhecloud.cube.biz.mdata.client.enumeration;

/**
 * 商户资料认证枚举
 */
public enum MerchantEnum {

    /**
     * 商户未认证
     */
    AUTH_UNAUTHORIZED,

    /**
     * 商户资料待审核
     */
    AUTH_AUDIT,

    /**
     * 商户资料已认证
     */
    AUTH_PASSED,

    /**
     * 商户资料不通过
     */
    AUTH_FAILED,

    /**
     * 商户资料失效
     */
    AUTH_INVALID,

    /**
     * 个人
     */
    TYPE_PERSONAL,

    /**
     * 企业
     */
    TYPE_ENTERPRISE,

    /**
     * 卖家
     */
    BIZ_TYPE_SELLER,

    /**
     * 买家
     */
    BIZ_TYPE_BUYER,
    ;

}
