package com.guhecloud.cube.biz.meattrade.server.base.entity;

import com.guhecloud.cube.biz.meattrade.server.base.enumeration.ConfigEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 基础配置
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Config extends BasicEnhancedTenantEntity<Long> {

    /**
     * 主键
     */
    private Long id;

    /**
     * 业务类型
     */
    private ConfigEnum type;

    /**
     * 业务数值
     */
    private Float value;

}
