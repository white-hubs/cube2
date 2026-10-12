package com.guhecloud.cube.base.dictionary.param;

import lombok.Data;

@Data
public class CascadeDictionaryDetailParam {
    /**
     * 字典编码
     */
    private String code;
    /**
     * 字典项编码
     */
    private String itemCode;
    /**
     * 字典名称（精准查询）
     */
    private String name;

}
