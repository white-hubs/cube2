package com.guhecloud.cube.customized.guhecloudwebsite.param;

import lombok.Data;

/**
 * 新增布局参数
 */
@Data
public class SysSiteLayoutAddParam {

    /** 布局名称 */
    private String layoutName;

    /** 布局配置JSON */
    private String config;
}
