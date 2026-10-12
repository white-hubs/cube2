package com.guhecloud.cube.base.dictionary.param;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class DataDictionaryaramByItemAndCodeParam {
    @NotEmpty(message = "字典项不能为空")
    private String itemCode;
    @NotEmpty(message = "字典编码不能为空")
    private String code;
}
