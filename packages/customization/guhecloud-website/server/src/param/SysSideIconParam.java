package com.guhecloud.cube.customized.guhecloudwebsite.param;

import lombok.Data;

/**
 * 侧边图标新增/编辑参数
 */
@Data
public class SysSideIconParam {

    /**
     * 标题
     */
    private String title;

    /**
     * 图标
     */
    private String icon;

    /**
     * 概述
     */
    private String summary;

    /**
     * 图片
     */
    private String image;

    /**
     * 跳转链接
     */
    private String link;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 状态：1显示，0隐藏
     */
    private Integer status;

    /**
     * 是否开启：1开启，0关闭
     */
    private Integer isOpen;

}
