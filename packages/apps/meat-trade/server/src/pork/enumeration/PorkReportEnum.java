package com.guhecloud.cube.biz.meattrade.server.pork.enumeration;

/**
 * @author lzy
 * @date 2021/12/4
 */
public enum PorkReportEnum {

    /**
     * 来货报备通过
     */
    PASSED,

    /**
     * 来货报备未通过
     */
    FAILED,

    /**
     * 来货报备撤销
     */
    RESCINDED,

    /**
     * 来货报备审核中
     */
    AUDIT,

    /**
     * 来货报备失效
     */
    INVALID,
}
