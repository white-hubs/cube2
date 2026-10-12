package com.guhecloud.cube.base.iot.product.model.v3.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 导入模式枚举
 *
 * 定义物模型导入时的模式选项
 */
@Getter
@AllArgsConstructor
public enum ImportModeEnum {

    /**
     * 替换模式
     */
    REPLACE("replace", "替换模式"),

    /**
     * 合并模式
     */
    MERGE("merge", "合并模式");

    /**
     * 模式编码
     */
    private final String code;

    /**
     * 模式描述
     */
    private final String desc;

    /**
     * 根据code获取枚举
     *
     * @param code 模式编码
     * @return 对应的枚举值,如果不存在则返回null
     */
    public static ImportModeEnum fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (ImportModeEnum mode : values()) {
            if (mode.code.equals(code)) {
                return mode;
            }
        }
        return null;
    }
}
