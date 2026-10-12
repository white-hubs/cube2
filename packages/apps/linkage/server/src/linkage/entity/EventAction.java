package com.guhecloud.cube.biz.linkage.server.linkage.entity;

import com.guhecloud.apps.linkage.metadata.enumeration.ExecuteStatusEnum;
import com.guhecloud.apps.linkage.metadata.enumeration.LinkageActionTypeEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 事件动作数据模型
 *
 * @author lanchun
 * @date 2022/6/21 13:50
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class EventAction extends BasicEnhancedTenantEntity<Long> {

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
     * 动作类型
     */
    private LinkageActionTypeEnum actionType;

    /**
     * 动作json
     */
    private String actionConfig;

    /**
     * 执行状态
     */
    private ExecuteStatusEnum executeStatus;
}
