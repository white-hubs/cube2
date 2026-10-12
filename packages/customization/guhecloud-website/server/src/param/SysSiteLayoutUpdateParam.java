package com.guhecloud.cube.customized.guhecloudwebsite.param;

import lombok.Data;

/**
 * 编辑布局参数
 */
@Data
public class SysSiteLayoutUpdateParam {

    /** 布局ID */
    private Long id;

    /** 布局名称 */
    private String layoutName;

    /** 布局配置JSON */
    private String config;
}
