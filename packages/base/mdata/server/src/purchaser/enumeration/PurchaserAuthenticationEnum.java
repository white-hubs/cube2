package com.guhecloud.cube.biz.mdata.server.purchaser.enumeration;

/**
 * @author lzy
 * @date 2021/12/24
 */
public enum PurchaserAuthenticationEnum {

    /**
     * 已失效
     */
    INVALID,

    /**
     * 未认证
     */
    UNAUTHORIZED,

    /**
     * 待审核
     */
    AUDITED,

    /**
     * 已认证
     */
    CERTIFIED,

    /**
     * 未通过
     */
    FAILED;

}
