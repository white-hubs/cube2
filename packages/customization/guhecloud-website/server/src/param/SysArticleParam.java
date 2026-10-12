package com.guhecloud.cube.customized.guhecloudwebsite.param;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 文章新增/编辑参数
 */
@Data
public class SysArticleParam {

    /** 文章标题 */
    @NotBlank(message = "文章标题不能为空")
    private String title;

    /** 分类ID */
    @NotNull(message = "分类ID不能为空")
    private Long categoryId;

    /** 封面图片URL */
    private String coverImageUrl;

    /** 摘要 */
    private String summary;

    /** 内容 */
    private String content;

    /** 作者 */
    private String author;

    /** 状态 1显示 0不显示 */
    private Integer status;
    /** 发布日期 */
    private String date;

    /** 排序 */
    private Integer sort;

}
