package com.guhecloud.cube.biz.linkage.server.linkage.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 事件关联联动预案数据模型
 *
 * @author lanchun
 * @date 2022/6/21 11:58
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class EventLinkage extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 事件主键
     */
    private Long eventId;

    /**
     * 联动策略主键
     */
    private Long linkageId;

    /**
     * 联动策略编号
     */
    private String linkageCode;

    /**
     * 联动策略名称
     */
    private String linkageName;

}
