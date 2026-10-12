package com.guhecloud.cube.biz.mdata.server.grid.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 网格交接状态
 */
@Getter
@AllArgsConstructor
public enum GridHandoverStatusEnum {
    /**
     * 待接班
     */
    WAIT_HANDOVER("待接班"),
    /**
     * 已交班
     */
    HAND_OVERED("已交班"),
    /**
     * 已接班
     */
    TAKE_OVERED("已接班"),
    /**
     * 交接班
     */
    HAND_TAKE("交接班"),
    /**
     * 已撤销
     */
    CANCEL("已撤销");

    private final String desc;
}
