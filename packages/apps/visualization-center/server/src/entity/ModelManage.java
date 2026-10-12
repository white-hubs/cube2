package com.guhecloud.cube.base.visualization.server.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2023-08-29  10:54
 * @Description: 模型管理
 * @Version: 1.0
 */
@Data
@TableName("model_manage")
public class ModelManage extends BasicEnhancedTenantEntity<Long> {
    @TableId
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
}
