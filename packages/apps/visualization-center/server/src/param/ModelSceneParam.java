package com.guhecloud.cube.base.visualization.server.param;

import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/1 18:13
 * @Description:
 */
@Data
public class ModelSceneParam {

    private Long id;

    /**
     * 项目id
     */
    private Long projectId;

    /**
     * 模型名称
     */
    private String name;

    /**
     * 模型地址
     */
    private String modelUrl;

    /**
     * 模型区域
     */
    private Long areaId;

    /**
     * 模型结构
     */
    private String modelStructure;

    /**
     * 业务菜单
     */
    private Long sceneTypeId;

    /**
     * 模型文件
     */
    private Long modelFile;

    /**
     * 组态链接
     */
    private String sceneUrl;

    /**
     * 组态图片
     */
    private Long sceneImg;
}
