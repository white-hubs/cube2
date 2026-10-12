package com.guhecloud.cube.gm.meattrade.plugin.couple.domain.enumeration;

import lombok.Getter;

/**
 * 挂钩号状态枚举
 *
 * @author lanchun
 * @date 2023/7/25 20:25
 */
@Getter
public enum CoupleStatusEnum {

    /**
     * 未交易
     */
    NON_TRADE,

    /**
     * 交易完成
     */
    TRADE_COMPLETED;
}
