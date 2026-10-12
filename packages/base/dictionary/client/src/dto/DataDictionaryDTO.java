package com.guhecloud.cube.base.dictionary.dto;

import lombok.Data;

@Data
public class DataDictionaryDTO {
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 编号
     */
    private String code;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 版本号
     */
    private String version;
    /**
     * 变量
     */
    private String variable;
    /**
     * 字典项变量
     */
    private String itemCode;

    /**
     * 状态
     */
    private String status;
}
