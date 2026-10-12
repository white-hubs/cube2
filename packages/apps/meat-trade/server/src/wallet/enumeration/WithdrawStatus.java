package com.guhecloud.cube.biz.meattrade.server.wallet.enumeration;

/**
 * 提现状态枚举
 *
 * @author Woody
 * @date 2021/8/23
 */
public enum WithdrawStatus {
    /**
     * 审核中
     */
    AUDITING,

    /**
     * 已通过
     */
    PASSED,

    /**
     * 已拒绝
     */
    REJECTED,

    /**
     * 已取消
     */
    CANCELED,

    /**
     * 已完成
     */
    COMPLETED;
}
