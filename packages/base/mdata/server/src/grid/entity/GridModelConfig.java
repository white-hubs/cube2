package com.guhecloud.cube.biz.mdata.server.grid.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 网格模型全局配置表
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "grid_model_config")
public class GridModelConfig extends BasicEnhancedTenantEntity<Long> {
    /**
     * 主键
     */
    private Long id;

    /**
     * 模型项目id
     */
    private Long modelItemId;
}

