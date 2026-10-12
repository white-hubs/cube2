package com.guhecloud.cube.biz.ouaa.server.param;

import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/8/5 11:12
 * @Description:
 */
@Data
public class ApplicationExtendInfo {
    /**
     * 应用ID
     */
    private Long id;
    /**
     * 应用介绍
     */
    private String description;

    /**
     * 功能描述
     */
    private String featureDesc;

    /**
     * 使用帮助
     */
    private String helpInfo;

    /**
     * 购买说明
     */
    private String purchaseInfo;

    /**
     * 标题
     */
    private String title;

    /**
     * 关键字
     */
    private String keywords;

    /**
     * 描述
     */
    private String appDesc;
}
