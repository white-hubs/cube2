package com.guhecloud.cube.biz.todo.server.enumeration;

import lombok.Getter;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/10/29 17:03
 * @Description: 交班状态
 */
@Getter
public enum HandoverStatusEnum {

    WAIT_HANDOVER("待接班"),

    HAND_OVERED("已接班"),

    CANCEL("已撤销"),
    ;

    private final String desc;

    HandoverStatusEnum(String desc) {
        this.desc = desc;
    }
}
