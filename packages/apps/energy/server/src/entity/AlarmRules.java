package com.guhecloud.cube.srv.gen.energymanagement.server.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lrj
 * @Description 报警规则配置
 * @date 2021-12-30 15:12:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AlarmRules extends BasicEnhancedTenantEntity<Long> {

    //
    private Long id;
    /**
     * 消息id
     */
    private Long messageId;
    //
    private String alarmName;
    /**
     * 是否启用
     */
    private Boolean enabled;
    //
    private String alarmLevel;
    //报警类型
    private String alarmType;
    //告警规则名称
    private String alarmRule;
    //数值
    private String alarmValue;
    /**
     * 触发级别名称
     */
    private String alarmLevelName;

    /**
     * 触发类型名称
     */
    private String alarmTypeName;
}