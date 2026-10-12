package com.guhecloud.cube.biz.mdata.server.scene.entity;

import com.guhecloud.cube.biz.mdata.server.scene.enumeration.BusinessPanelConfigTypeEnum;
import com.guhecloud.cube.biz.mdata.server.scene.enumeration.BusinessPanelPositionEnum;
import com.guhecloud.platform.database.annotation.Unique;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 设施面板配置数据模型
 *
 * @author lanchun
 * @date 2022/4/1 14:22
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BusinessPanelConfig extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 所属项目id
     */
    private Long projectId;

    /**
     * 设施类型
     */
    private BusinessPanelConfigTypeEnum businessType;

    /**
     * 设施主键
     */
    @Unique(composites = { "businessType", "panelPosition", "deleted" })
    private Long businessId;

    /**
     * 面板定位
     */
    private BusinessPanelPositionEnum panelPosition;

    /**
     * 面板配置
     */
    private String panelConfig;

}
