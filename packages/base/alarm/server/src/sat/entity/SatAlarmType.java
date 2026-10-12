package com.guhecloud.cube.base.alarm.server.sat.entity;

import com.guhecloud.platform.database.annotation.Unique;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 平台报警类型数据模型
 *
 * @author lanchun
 * @date 2022/6/2 14:29
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SatAlarmType extends BasicEnhancedEntity<Long> {

    private Long id;

    /**
     * 编号
     */
    private String code;

    /**
     * 类型名称
     */
    @Unique(composites = { "deleted", "applicationCode" }, message = "类型名称已存在")
    private String name;

    /**
     * 应用编号
     */
    private String applicationCode;

    /**
     * 应用名称
     */
    private String applicationName;
}
