package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 巡检计划关联关系对象(将id查询成名称后的对象)
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PlanRelationDTO extends BasicEnhancedTenantEntity<Long> {
    private Long id;

    private Long planId;

    private String planName;

    private Long taskId;

    private String taskName;

    private String targetCategory;

    private String targetType;

    private Long targetId;

    private String targetName;

    private Long standardId;

    private String standardName;

    private Long itemId;

    private String itemName;

    private String itemContent;

    private String defaultResult;

    private String resultNames;

    private LocalDateTime inspectTime;
}
