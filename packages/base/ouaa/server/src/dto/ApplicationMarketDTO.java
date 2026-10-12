package com.guhecloud.cube.biz.ouaa.server.dto;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/8/6 16:42
 * @Description:
 */

import lombok.Data;

@Data
public class ApplicationMarketDTO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 应用名称
     */
    private String name;

    /**
     * 应用图标
     */
    private String icon;

    /**
     * 最新版本
     */
    private String version;
    /**
     * 应用介绍
     */
    private String description;
}
