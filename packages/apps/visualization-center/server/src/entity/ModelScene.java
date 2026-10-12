package com.guhecloud.cube.base.visualization.server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 模型场景配置
 *
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/1 14:59
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("model_scene")
public class ModelScene extends BasicEnhancedTenantEntity<Long> {

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
     * 模型地址（渲染引擎为UE时使用）
     */
    private String modelUrl;

    /**
     * 区域
     */
    private Long areaId;

    /**
     * 模型结构
     */
    private String modelStructure;

    /**
     * 模型文件 （WEBGL使用）
     */
    private Long modelFile;

    /**
     * 业务菜单 （二位组态）
     */
    private Long sceneTypeId;

    /**
     * 组态链接
     */
    private String sceneUrl;

    /**
     * 组态图片
     */
    private Long sceneImg;
}
