package com.guhecloud.cube.base.iot.product.model.v3.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 操作类型枚举
 *
 * 定义物模型操作的类型
 */
@Getter
@AllArgsConstructor
public enum OperationTypeEnum {

    /**
     * 创建
     */
    CREATE("CREATE", "创建"),

    /**
     * 更新
     */
    UPDATE("UPDATE", "更新"),

    /**
     * 删除
     */
    DELETE("DELETE", "删除"),

    /**
     * 发布
     */
    PUBLISH("PUBLISH", "发布"),

    /**
     * 回滚
     */
    ROLLBACK("ROLLBACK", "回滚");

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
    public static OperationTypeEnum fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (OperationTypeEnum type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}
