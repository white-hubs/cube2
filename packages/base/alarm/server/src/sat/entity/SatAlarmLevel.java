package com.guhecloud.cube.base.alarm.server.sat.entity;

import com.guhecloud.platform.database.annotation.Unique;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 平台报警等级数据模型
 *
 * @author lanchun
 * @date 2022/6/2 14:12
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SatAlarmLevel extends BasicEnhancedEntity<Long> {

    private Long id;

    /**
     * 编号
     */
    private String code;

    /**
     * 名称
     */
    @Unique(composites = "deleted", message = "名称已存在")
    private String name;

    /**
     * 序号
     */
    private Integer sort;
}
