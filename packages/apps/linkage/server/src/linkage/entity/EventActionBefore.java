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
public class EventActionBefore extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 事件主键
     */
    private Long eventId;

    /**
     * 联动预案主键
     */
    private Long linkageId;

    /**
     * 事件关联预案主键
     */
    private Long eventLinkageId;

    /**
     * 类型
     */
    private ActionBeforeTypeEnum actionBeforeType;

    /**
     * json配置
     */
    private String actionBeforeConfig;
}
