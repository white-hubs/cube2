package com.guhecloud.cube.customized.guhecloudwebsite.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文章分类查询参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysArticleCategoryQueryParam extends Paging {

    /** 分类名称（模糊） */
    private String categoryName;

    /** 上级分类ID */
    private Long parentId;

    /** 状态 1启用 0禁用 */
    private Integer status;

}
