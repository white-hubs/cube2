package com.guhecloud.cube.srv.gen.work.permit.server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 作业类型配置。
 */
@Data
@TableName("work_permit_type_config")
@EqualsAndHashCode(callSuper = true)
public class WorkPermitTypeConfig extends BasicEnhancedTenantEntity<Long> {
    /**
     * 主键。
     */
    private Long id;
    /**
     * 作业类型名称。
     */
    private String typeName;
    /**
     * 作业等级。
     */
    private String workPermitLevel;
    /**
     * 等级定义。
     */
    private String levelDefinition;
    /**
     * 作业票有效时长，单位小时。
     */
    private Integer permitValidHours;
}
