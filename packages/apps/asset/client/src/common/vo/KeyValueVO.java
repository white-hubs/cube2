package com.guhecloud.cube.property.management.client.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * key-valus 通用实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeyValueVO {
    /**
     * 名称
     */
    private String name;
    /**
     * 值
     */
    private Long value;

    /**
     * 字符串的值
     */
    private String strValue;

    public KeyValueVO(String name, String strValue) {
        this.name = name;
        this.strValue = strValue;
    }
}
