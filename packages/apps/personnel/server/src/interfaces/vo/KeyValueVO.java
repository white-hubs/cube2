package com.guhecloud.cube.srv.gen.personnel.management.server.interfaces.vo;

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
}
