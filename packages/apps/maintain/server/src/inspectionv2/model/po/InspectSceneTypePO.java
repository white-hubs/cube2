package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wql
 * @describe 现场点类型
 * @date 2023/6/5 15:45
 */
@Data
@TableName("inspect_scene_type")
@EqualsAndHashCode(callSuper = true)
@Builder
public class InspectSceneTypePO extends BasicEnhancedTenantEntity<Long> {
    /**
     * 现场点类型id
     */
    private Long id;
    /**
     * 现场点类型名称
     */
    private String name;

}
