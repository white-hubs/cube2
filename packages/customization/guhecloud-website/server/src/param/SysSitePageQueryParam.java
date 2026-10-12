package com.guhecloud.cube.customized.guhecloudwebsite.param;

import lombok.Data;

/**
 * 页面管理查询参数
 */
@Data
public class SysSitePageQueryParam {

    /**
     * 页面名称（模糊）
     */
    private String metaTitle;
    /**
     * 状态
     */
    private String status;

}
