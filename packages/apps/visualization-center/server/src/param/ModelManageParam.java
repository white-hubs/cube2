package com.guhecloud.cube.base.visualization.server.param;

import lombok.Data;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2023-08-29  10:56
 * @Description:
 * @Version: 1.0
 */
@Data
public class ModelManageParam {
    private Long id;
    /**
     * 模型名称
     */
    private String name;
    /**
     * 模型类型
     */
    private String modelType;
    /**
     * 模型地址
     */
    private String address;
    /**
     * 模型封面
     */
    private Long file;
    /**
     * 模型导航
     */
    private List<ModelRouteParam> modelRoute;
}
