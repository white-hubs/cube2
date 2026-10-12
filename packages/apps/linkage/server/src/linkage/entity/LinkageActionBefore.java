package com.guhecloud.cube.biz.linkage.server.linkage.entity;

import com.guhecloud.apps.linkage.metadata.enumeration.ActionBeforeTypeEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class LinkageActionBefore extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 联动预案主键
     */
    private Long linkageId;

    /**
     * 动作类型
     */
    private ActionBeforeTypeEnum actionBeforeType;

    /**
     * json配置
     */
    private String actionBeforeConfig;

    /**
     * 启用状态
     */
    private Boolean enabled;
}
