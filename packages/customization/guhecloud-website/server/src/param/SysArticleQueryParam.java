package com.guhecloud.cube.customized.guhecloudwebsite.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

/**
 * 文章分页查询参数
 */
@Data
public class SysArticleQueryParam extends Paging {

    /**
     * 文章分类ID
     */
    private Long categoryId;

    /**
     * 状态 1显示 0不显示
     */
    private Integer status;

    /**
     * 文章标题（模糊查询）
     */
    private String title;

}
