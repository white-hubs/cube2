package com.guhecloud.cube.base.dictionary.server.param;

import lombok.Data;

import jakarta.validation.constraints.Size;

@Data
public class DictionaryItemParam {
    /**
     * 编码
     */
    @Size(min = 5, max = 100, message = "字典项长度只能为5-100")
    private String code;
    /**
     * 备注
     */
    @Size(min = 5, max = 100, message = "备注长度只能为5-100")
    private String remarks;
    /**
     * 变更通知方式
     */
    private String changeNotice;
}
