package com.guhecloud.cube.srv.gen.property.management.server.risk.source.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2024-04-07  13:58
 * @Description:
 * @Version: 1.0
 */
@TableName("risk_source_bim_config")
@Data
public class RiskSourceBimConfig extends BasicEnhancedTenantEntity<Long> {
    /**
     * 主键
     */
    private Long id;
    /**
     * 场景类型
     */
    private String sceneType;
    /**
     * 场景类型序列号
     */
    private String sceneTypeSequence;
    /**
     * 业务类型
     */
    private String businessType;
    /**
     * 业务主键
     */
    private String businessId;
}
