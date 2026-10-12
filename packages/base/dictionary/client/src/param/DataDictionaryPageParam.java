package com.guhecloud.cube.base.dictionary.param;

import com.guhecloud.platform.base.model.Paging;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
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
