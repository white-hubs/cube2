package com.guhecloud.cube.base.iot.product.model.v3.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 导出类型枚举
 *
 * 定义物模型导出时的类型选项
 */
@Getter
@AllArgsConstructor
public enum ExportTypeEnum {

    /**
     * 导出全部
     */
    ALL("all", "导出全部"),

    /**
     * 仅导出属性
     */
    PROPERTIES_ONLY("properties_only", "仅导出属性"),

    /**
     * 仅导出事件
     */
    EVENTS_ONLY("events_only", "仅导出事件"),

    /**
     * 仅导出方法
     */
    METHODS_ONLY("methods_only", "仅导出方法");

    /**
     * 类型编码
     */
    private final String code;

    /**
     * 类型描述
     */
    private final String desc;

    /**
     * 根据code获取枚举
     *
     * @param code 类型编码
     * @return 对应的枚举值,如果不存在则返回null
     */
    public static ExportTypeEnum fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (ExportTypeEnum type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}
