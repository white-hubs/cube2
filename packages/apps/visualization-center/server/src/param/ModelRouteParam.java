package com.guhecloud.cube.base.visualization.server.param;

import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2023-08-29  14:48
 * @Description:
 * @Version: 1.0
 */
@Data
public class ModelRouteParam {
    private Long id;
    /**
     * 模型导航名称
     */
    private String name;
    /**
     * 模型导航编号
     */
    private String code;
    /**
     * 层级
     */
    private Integer level;
    /**
     * 父类主键
     */
    private Long parentId;
    /**
     * 序号
     */
    private Integer serial;
    /**
     * 模型地址
     */
    private String url;
    /**
     * 模型导航目录
     */
    private String directory;
    /**
     * 模型id
     */
    private Long modelId;
}
