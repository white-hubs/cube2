package com.guhecloud.cube.base.dictionary.server.param;

import lombok.Data;

import java.util.Set;

@Data
public class CascadeDictionaryParam {
    /**
     * 字典id
     */
    private Long id;
    /**
     * 字典项编码
     */
    private String itemCode;
    /**
     * 字典编码
     */
    private Set<String> codes;
    /**
     * 是否查询禁用的字典
     */
    private Boolean disable = false;
    /**
     * 父级id
     */
    private Long superiorsId;
    /**
     * 关键字
     */
    private String keyword;

}
