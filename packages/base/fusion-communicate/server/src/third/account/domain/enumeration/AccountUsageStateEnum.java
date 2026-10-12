package com.guhecloud.cube.biz.fusion.communicate.server.third.account.domain.enumeration;

import lombok.Getter;

/**
 * 通讯账号使用状态枚举
 *
 * @author lanchun
 * @date 2024/7/29 15:34
 */
@Getter
public enum AccountUsageStateEnum {

    /**
     * 未使用
     */
    UNUSED(1),

    /**
     * 使用中
     */
    IN_USED(2);

    private int order;

    public int getOrder() {
        return order;
    }

    AccountUsageStateEnum(int order) {
        this.order = order;
    }
}
