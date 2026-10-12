package com.guhecloud.cube.biz.meattrade.server.base.param;

import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 字典新增信息
 */
@Data
public class DictionaryCreateParam {

    /**
     * 类型
     */
    @NotNull(message = "类型不能为空")
    @Size(max = 100)
    private String type;

    /**
     * 编号
     */
    @NotNull(message = "编号不能为空")
    @Size(max = 100)
    private String code;

    /**
     * 名称
     */
    @NotNull(message = "名称不能为空")
    @Size(max = 100)
    private String name;

    /**
     * 备注
     */
    @Size(max = 100)
    private String remark;

    /**
     * 排序
     */
    @Min(1)
    private Float sort;

}
