package com.guhecloud.cube.base.dataintegrate.server.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Appet
 * @Description 报警数据采集配置
 * @date 2023-09-13 15:08:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AlarmPushConfig extends BasicEnhancedTenantEntity<Long> {

    //
    private Long id;
    /**
     * 报警类型编号
     */
    private String alarmTypeCode;
    /**
     * 报警级别编号
     */
    private String alarmLevelCode;

    /**
     * 是否超时
     */
    private Boolean timeout;
}