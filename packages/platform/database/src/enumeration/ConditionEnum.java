package com.guhecloud.platform.database.enumeration;

import lombok.Getter;

/**
 * 条件枚举
 *
 * @author lanchun
 * @date 2024/11/26 9:33
 */
@Getter
public enum ConditionEnum {

    /**
     * 等于
     */
    EQ,

    /**
     * 包含
     */
    IN,

    /**
     * 不包含(暂无实现)
     */
    NOT_IN,

    /**
     * 不等于(暂无实现)
     */
    NE,

    /**
     * 包含
     */
    LIKE,

}
