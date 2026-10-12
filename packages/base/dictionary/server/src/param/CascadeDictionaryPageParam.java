package com.guhecloud.cube.base.dictionary.server.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotEmpty;

@Data
@EqualsAndHashCode(callSuper = true)
public class CascadeDictionaryPageParam extends Paging {
    /**
     * 字典项编码
     */
    @NotEmpty(message = "字典项编码不能为空")
    private String itemCode;
    /**
     * 名称
     */
    private String name;
    /**
     * 编码
     */
    private String code;
    /**
     * 父级id，为空则查第一级
     */
    private Long superiorsId;
}
