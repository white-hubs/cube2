package com.guhecloud.cube.biz.mdata.server.scene.entity;

import com.guhecloud.platform.database.annotation.Unique;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantTreeEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 场景类型信息数据模型
 *
 * @author lanchun
 * @date 2021/12/2 17:39
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SceneType extends BasicEnhancedTenantTreeEntity<Long> {

    /**
     * 主键
     */
    private Long id;

    /**
     * 所属项目id
     */
    private Long projectId;

    /**
     * 场景类型名称
     */
    @Unique(composites = { "projectId", "deleted" }, message = "场景名称已存在")
    private String name;

    /**
     * 场景类型编号
     */
    @Unique(composites = { "projectId", "deleted" }, message = "场景编号已存在")
    private String code;

    /**
     * 场景序号
     */
    private Integer sort;

    /**
     * 备注
     */
    private String remark;

    //场景类型
    private String scenarioType;

}
