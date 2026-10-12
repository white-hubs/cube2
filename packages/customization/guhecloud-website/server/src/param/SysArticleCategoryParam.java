package com.guhecloud.cube.customized.guhecloudwebsite.param;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 文章分类新增/编辑参数
 */
@Data
public class SysArticleCategoryParam {

    /** 分类名称 */
    @NotBlank(message = "分类名称不能为空")
    private String categoryName;

    /** 上级分类ID */
    @NotNull(message = "上级分类不能为空")
    private Long parentId;

    /** 排序 */
    private Integer sort;

    /** 状态 1启用 0禁用 */
    private Integer status;

}
