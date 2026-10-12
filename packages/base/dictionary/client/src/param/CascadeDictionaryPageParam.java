package com.guhecloud.cube.base.dictionary.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;

@Data
public class CascadeDictionaryPageParam extends Paging {
    /**
     * 字典项编码
     */
    @NotEmpty(message = "字典项编码不能为空")
    private String itemCode;
    private String name;
    private String code;
    /**
     * 父级id，为空则查第一级
     */
    private Long superiorsId;
}
