package com.guhecloud.cube.biz.mdata.server.scene.entity;

import com.guhecloud.platform.database.annotation.Unique;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 区域场景配置实体数据模型
 *
 * @author lanchun
 * @date 2021/11/30 20:31
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AreaSceneConfig extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 所属项目id
     */
    private Long projectId;

    /**
     * 区域主键
     */
    @Unique(composites = { "sceneType", "deleted" }, message = "该区域已存在相同场景配置")
    private Long areaId;

    /**
     * 场景类型
     */
    private Long sceneType;

    /**
     * 场景类型序列号
     */
    private String sceneTypeSequence;

    /**
     * 模型视角绑定主键
     */
    private String bimViewBindIds;

}
