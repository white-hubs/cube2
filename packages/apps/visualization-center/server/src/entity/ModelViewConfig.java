package com.guhecloud.cube.base.visualization.server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 模型视角配置
 *
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/1 18:31
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("model_view_config")
public class ModelViewConfig extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 项目id
     */
    private Long projectId;

    /**
     * 视角名称
     */
    private String label;

    /**
     * 视角参数
     */
    private String viewValue;

    /**
     * 区域id
     */
    private Long areaId;

    /**
     * 中心点位置
     */
    private String buildTagLocation;

    /**
     * 是否默认
     */
    private String isDefault;

    /**
     * 隐藏区域
     */
    private String hideActors;
}
