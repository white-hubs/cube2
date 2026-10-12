package com.guhecloud.cube.biz.mdata.client.enumeration;

/**
 * @author lzy
 * @date 2021/12/3
 */
public enum SupplierAuthenticatedEnum {

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
