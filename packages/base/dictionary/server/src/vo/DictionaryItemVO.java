package com.guhecloud.cube.base.dictionary.server.vo;

import lombok.Data;

@Data
public class DictionaryItemVO {
    private Long id;
    /**
     * 字典项编码
     */
    private String code;

    /**
     * 说明
     */
    private String remarks;
    /**
     * 变更通知方式
     */
    private String changeNotice;
}
