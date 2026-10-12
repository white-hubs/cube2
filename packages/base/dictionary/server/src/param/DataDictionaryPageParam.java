package com.guhecloud.cube.base.dictionary.server.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotEmpty;

@Data
@EqualsAndHashCode(callSuper = true)
public class DataDictionaryPageParam extends Paging {
    @NotEmpty(message = "字典项不能为空")
    private String itemCode;
    /**
     * 字典名称
     */
    private String name;
    /**
     * 字典编码
     */
    private String code;
    /**
     * 是否查询禁用数据，默认false
     */
    private Boolean disable;

}
