package com.guhecloud.cube.base.dictionary.server.param;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

@Data
public class SysDictionaryaramParam {
    @NotEmpty(message = "字典项不能为空")
    private String itemCode;
    /**
     * 字典名称
     */
    private String name;
    /**
     * 字典编码（模糊匹配）
     */
    private String code;
    /**
     * 是否查询禁用数据，默认false
     */
    private Boolean disable;

    /**
     * 字典编码集合
     */
    private Set<String> codes;
}
