package com.guhecloud.cube.base.visualization.server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2023-08-29  11:32
 * @Description: 模型导航路径
 * @Version: 1.0
 */
@Data
@TableName("model_route")
public class ModelRoute extends BasicEnhancedTenantEntity<Long> {
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
    private String tag;
}
