package com.guhecloud.cube.biz.ouaa.server.enumeration;

import lombok.Getter;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/8/6 18:01
 * @Description: 迭代类别
 */
@Getter
public enum IterationCategoryEnum {

    /**
     * 修复
     */
    FIX(0),

    /**
     * 优化
     */
    OPTIMIZE(1),

    /**
     * 新增
     */
    ADD(2),

    /**
     * 移除
     */
    REMOVE(3);

    private final Integer code;

    IterationCategoryEnum(Integer code) {
        this.code = code;
    }

}
