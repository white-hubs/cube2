package com.guhecloud.cube.srv.gen.product.market.server.pricing.enumeration;

import lombok.Getter;

/**
 * 计划状态
 */
public enum TaskStatusEnum {

    EXEC("执行中"),

    WAIT("待接收"),

    OVER("已结束");

    @Getter
    String label;

    TaskStatusEnum(String label) {
        this.label = label;
    }

}
