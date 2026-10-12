package com.guhecloud.cube.base.visualization.server.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/1 16:08
 * @Description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModelSceneVO {

    /**
     * 主键id
     */
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
     * 模型区域名称
     */
    private String areaName;

    /**
     * 模型结构（UE5）
     */
    private String modelStructure;

    /**
     * 业务菜单(二维组态)
     */
    private Long sceneTypeId;

    /**
     * 业务菜单名称(二维组态)
     */
    private String sceneTypeName;

    /**
     * 模型文件(WEBGL)
     */
    private FileVO modelFile;

    /**
     * 组态链接
     */
    private String sceneUrl;

    /**
     * 组态图片
     */
    private FileVO sceneImg;
}
