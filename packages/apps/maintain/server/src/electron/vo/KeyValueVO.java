package com.guhecloud.cube.srv.gen.maintain.server.electron.vo;

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
    private Integer value;

    /**
     * 字符串的值
     */
    private String strValue;
}
