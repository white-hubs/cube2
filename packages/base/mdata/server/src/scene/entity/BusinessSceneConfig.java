package com.guhecloud.cube.biz.mdata.server.scene.entity;

import com.guhecloud.cube.biz.mdata.server.scene.enumeration.BusinessSceneConfigTypeEnum;
import com.guhecloud.platform.database.annotation.Unique;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BusinessSceneConfig extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 所属项目id
     */
    private Long projectId;

    /**
     * 场景类型
     */
    private Long sceneType;

    /**
     * 场景类型序列号
     */
    private String sceneTypeSequence;

    /**
     * 业务类型
     */
    private BusinessSceneConfigTypeEnum businessType;

    /**
     * 业务主键
     */
    @Unique(composites = { "sceneType", "deleted" }, message = "该设施已存在相同场景配置")
    private Long businessId;

}
