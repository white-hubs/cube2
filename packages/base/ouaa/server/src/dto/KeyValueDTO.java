package com.guhecloud.cube.biz.ouaa.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * key-valus 通用实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeyValueDTO {
    /**
     * 名称
     */
    private String name;
    /**
     * 值
     */
    private Integer value;
}
