package com.guhecloud.cube.srv.gen.product.market.server.pricing.enumeration;

import lombok.Getter;

/**
 * 计划状态
 */
public enum PlanStatusEnum {

    EXEC("执行中"),

    STOP("已停用"),

    OVER("已结束");

    @Getter
    String label;

    PlanStatusEnum(String label) {
        this.label = label;
    }

}
